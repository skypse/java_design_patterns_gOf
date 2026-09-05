package com.gfb.designpatterns.structural.facade;

import com.gfb.designpatterns.behavioral.observer.CustomerAlertNotifier;
import com.gfb.designpatterns.behavioral.observer.InventoryStockManager;
import com.gfb.designpatterns.behavioral.observer.OrderEventManager;
import com.gfb.designpatterns.behavioral.observer.OrderEventType;
import com.gfb.designpatterns.behavioral.strategy.ShippingCalculator;
import com.gfb.designpatterns.behavioral.strategy.ShippingStrategy;
import com.gfb.designpatterns.creational.factory.Notification;
import com.gfb.designpatterns.creational.factory.NotificationFactory;
import com.gfb.designpatterns.creational.singleton.ConfigurationManager;
import com.gfb.designpatterns.structural.facade.subsystems.CustomerService;
import com.gfb.designpatterns.structural.facade.subsystems.CustomerService.Customer;
import com.gfb.designpatterns.structural.facade.subsystems.LogisticsService;
import com.gfb.designpatterns.structural.facade.subsystems.LogisticsService.DispatchInfo;
import com.gfb.designpatterns.structural.facade.subsystems.PaymentGateway;
import com.gfb.designpatterns.structural.facade.subsystems.PaymentGateway.PaymentMethod;
import com.gfb.designpatterns.structural.facade.subsystems.PaymentGateway.PaymentResult;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Padrão Facade.
 * Fornece uma interface unificada e simplificada para um conjunto complexo
 * de subsistemas (Cliente, Pagamento, Logística, Cálculo de Frete e Notificações).
 */
public class OrderCheckoutFacade {

    private final CustomerService customerService;
    private final PaymentGateway paymentGateway;
    private final LogisticsService logisticsService;
    private final OrderEventManager eventManager;

    public OrderCheckoutFacade() {
        this.customerService = new CustomerService();
        this.paymentGateway = new PaymentGateway();
        this.logisticsService = new LogisticsService();
        this.eventManager = new OrderEventManager();
    }

    public record CheckoutSummary(
            String orderId,
            boolean success,
            BigDecimal totalAmount,
            String trackingCode,
            String message
    ) {}

    /**
     * Fluxo completo de checkout orquestrado através da fachada.
     */
    public CheckoutSummary checkout(
            String customerId,
            BigDecimal itemsAmount,
            ShippingStrategy shippingStrategy,
            double weightInKg,
            double distanceInKm,
            PaymentMethod paymentMethod
    ) {
        String orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        System.out.printf("%n=== [FACADE] Iniciando Processo de Checkout Simplificado (Pedido %s) ===%n", orderId);

        // 1. Validar e obter dados do cliente
        Customer customer = customerService.findCustomerById(customerId);
        if (!customerService.validateCustomer(customer)) {
            return new CheckoutSummary(orderId, false, BigDecimal.ZERO, null, "Cliente inválido ou não localizado.");
        }

        // Configurar observadores de evento do pedido para este cliente e para o armazém
        OrderEventManager checkoutEventManager = new OrderEventManager();
        CustomerAlertNotifier customerNotifier = new CustomerAlertNotifier(customer.email());
        InventoryStockManager stockManager = new InventoryStockManager();
        checkoutEventManager.subscribe(OrderEventType.PAYMENT_CONFIRMED, customerNotifier);
        checkoutEventManager.subscribe(OrderEventType.PAYMENT_CONFIRMED, stockManager);
        checkoutEventManager.subscribe(OrderEventType.ORDER_SHIPPED, customerNotifier);

        // 2. Calcular frete utilizando a Estratégia fornecida
        ShippingCalculator shippingCalculator = new ShippingCalculator(shippingStrategy);
        BigDecimal shippingCost = shippingCalculator.calculateShipping(weightInKg, distanceInKm);
        System.out.printf("  [Estratégia Frete] %s -> Custo: R$ %.2f (Estimativa: %d dias)%n",
                shippingStrategy.getServiceDescription(), shippingCost, shippingStrategy.getDeliveryDaysEstimate());

        BigDecimal totalAmount = itemsAmount.add(shippingCost);

        // 3. Processar Pagamento
        PaymentResult paymentResult = paymentGateway.processPayment(customerId, totalAmount, paymentMethod);
        if (!paymentResult.success()) {
            return new CheckoutSummary(orderId, false, totalAmount, null, "Falha no pagamento: " + paymentResult.message());
        }

        // Notificar ouvintes que o pagamento foi aprovado
        checkoutEventManager.notify(OrderEventType.PAYMENT_CONFIRMED, orderId, "Valor pago: R$ " + totalAmount);

        // 4. Despachar Pedido via Logística
        DispatchInfo dispatchInfo = logisticsService.dispatchOrder(orderId, customer.cep(), customer.city());

        // Notificar ouvintes que o pedido foi despachado
        checkoutEventManager.notify(OrderEventType.ORDER_SHIPPED, orderId, "Rastreio: " + dispatchInfo.trackingCode());

        // 5. Enviar Notificação final via Factory Method
        Notification confirmationNotification = NotificationFactory.createNotification(NotificationFactory.NotificationType.EMAIL);
        confirmationNotification.send(customer.email(),
                String.format("Seu pedido #%s foi aprovado e despachado! Código de Rastreio: %s", orderId, dispatchInfo.trackingCode()));

        ConfigurationManager config = ConfigurationManager.getInstance();
        System.out.printf("  [Sistema] Concluído sob a versão: %s (%s)%n",
                config.getProperty("app.version"), config.getProperty("app.environment"));

        return new CheckoutSummary(orderId, true, totalAmount, dispatchInfo.trackingCode(), "Pedido finalizado e despachado com sucesso!");
    }
}
