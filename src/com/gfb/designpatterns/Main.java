package com.gfb.designpatterns;

import com.gfb.designpatterns.behavioral.observer.CustomerAlertNotifier;
import com.gfb.designpatterns.behavioral.observer.InventoryStockManager;
import com.gfb.designpatterns.behavioral.observer.OrderEventManager;
import com.gfb.designpatterns.behavioral.observer.OrderEventType;
import com.gfb.designpatterns.behavioral.strategy.EconomyShipping;
import com.gfb.designpatterns.behavioral.strategy.ExpressShipping;
import com.gfb.designpatterns.behavioral.strategy.InStorePickupShipping;
import com.gfb.designpatterns.behavioral.strategy.ShippingCalculator;
import com.gfb.designpatterns.behavioral.strategy.ShippingStrategy;
import com.gfb.designpatterns.creational.factory.Notification;
import com.gfb.designpatterns.creational.factory.NotificationFactory;
import com.gfb.designpatterns.creational.factory.NotificationFactory.NotificationType;
import com.gfb.designpatterns.creational.singleton.AppTheme;
import com.gfb.designpatterns.creational.singleton.ConfigurationManager;
import com.gfb.designpatterns.structural.facade.OrderCheckoutFacade;
import com.gfb.designpatterns.structural.facade.subsystems.PaymentGateway.PaymentMethod;

import java.math.BigDecimal;

/**
 * Ponto de entrada da aplicação.
 * Demonstra de forma didática e interativa a execução dos 5 Padrões GoF implementados:
 * 1. Singleton (Lazy Holder & Enum)
 * 2. Factory Method
 * 3. Strategy
 * 4. Observer
 * 5. Facade (Orquestração de Ponta a Ponta)
 */
public class Main {

    public static void main(String[] args) {
        printBanner();

        demonstrateSingleton();
        demonstrateFactoryMethod();
        demonstrateStrategy();
        demonstrateObserver();
        demonstrateFacade();

        printFooter();
    }

    private static void printBanner() {
        System.out.println("================================================================================");
        System.out.println("          EXPLORANDO PADRÕES DE PROJETO GOF (GANG OF FOUR) EM JAVA PURO          ");
        System.out.println("                 Projeto Prático para Consolidação & Portfólio                  ");
        System.out.println("================================================================================");
    }

    private static void demonstrateSingleton() {
        System.out.println("\n--------------------------------------------------------------------------------");
        System.out.println(" 1. PADRÃO CRIACIONAL: SINGLETON (Lazy Holder & Enum)");
        System.out.println("--------------------------------------------------------------------------------");

        System.out.println("• Teste 1.1: ConfigurationManager (Lazy Holder - Thread Safe)");
        ConfigurationManager config1 = ConfigurationManager.getInstance();
        ConfigurationManager config2 = ConfigurationManager.getInstance();

        System.out.printf("  Config 1 HashCode: %d%n", config1.hashCode());
        System.out.printf("  Config 2 HashCode: %d%n", config2.hashCode());
        System.out.printf("  -> As instâncias são rigorosamente idênticas? %s%n", (config1 == config2));

        config1.setProperty("custom.banner.message", "Bem-vindo ao Lab de Design Patterns!");
        System.out.printf("  -> Valor lido através de config2: \"%s\"%n", config2.getProperty("custom.banner.message"));

        System.out.println("\n• Teste 1.2: AppTheme (Enum Singleton)");
        AppTheme theme = AppTheme.DARK_MODE;
        theme.applyTheme();
    }

    private static void demonstrateFactoryMethod() {
        System.out.println("\n--------------------------------------------------------------------------------");
        System.out.println(" 2. PADRÃO CRIACIONAL: FACTORY METHOD (Notification Factory)");
        System.out.println("--------------------------------------------------------------------------------");

        System.out.println("• Criando e despachando diferentes tipos de notificações dinamicamente:");

        Notification email = NotificationFactory.createNotification(NotificationType.EMAIL);
        email.send("usuario@empresa.com", "Seu relatório mensal de vendas está disponível.");

        Notification sms = NotificationFactory.createNotification(NotificationType.SMS);
        sms.send("+55 11 99999-8888", "Código de verificação 2FA: 482-910.");

        Notification push = NotificationFactory.createNotification(NotificationType.PUSH);
        push.send("device-token-xyz-123", "Você recebeu um novo cupom de desconto de 20%!");
    }

    private static void demonstrateStrategy() {
        System.out.println("\n--------------------------------------------------------------------------------");
        System.out.println(" 3. PADRÃO COMPORTAMENTAL: STRATEGY (Cálculo Dinâmico de Frete)");
        System.out.println("--------------------------------------------------------------------------------");

        double weight = 2.5; // kg
        double distance = 120.0; // km

        System.out.printf("• Simulando cálculo para carga de %.1f kg e distância de %.1f km:%n", weight, distance);

        ShippingCalculator calculator = new ShippingCalculator(new EconomyShipping());
        printShippingEstimate(calculator.getStrategy(), calculator.calculateShipping(weight, distance));

        // Mudança dinâmica de algoritmo em tempo de execução
        calculator.setStrategy(new ExpressShipping());
        printShippingEstimate(calculator.getStrategy(), calculator.calculateShipping(weight, distance));

        // Outra estratégia
        calculator.setStrategy(new InStorePickupShipping());
        printShippingEstimate(calculator.getStrategy(), calculator.calculateShipping(weight, distance));
    }

    private static void printShippingEstimate(ShippingStrategy strategy, BigDecimal cost) {
        System.out.printf("  -> %-40s | Prazo: %d dia(s) | Custo: R$ %.2f%n",
                strategy.getServiceDescription(),
                strategy.getDeliveryDaysEstimate(),
                cost);
    }

    private static void demonstrateObserver() {
        System.out.println("\n--------------------------------------------------------------------------------");
        System.out.println(" 4. PADRÃO COMPORTAMENTAL: OBSERVER (Barramento de Eventos de Pedido)");
        System.out.println("--------------------------------------------------------------------------------");

        OrderEventManager eventManager = new OrderEventManager();

        CustomerAlertNotifier customerAlice = new CustomerAlertNotifier("alice@tech.io");
        CustomerAlertNotifier customerBob = new CustomerAlertNotifier("bob@domain.org");
        InventoryStockManager stockService = new InventoryStockManager();

        // Registro de observadores para eventos específicos
        eventManager.subscribe(OrderEventType.PAYMENT_CONFIRMED, customerAlice);
        eventManager.subscribe(OrderEventType.ORDER_CANCELLED, customerBob);
        eventManager.subscribe(OrderEventType.PAYMENT_CONFIRMED, stockService);
        eventManager.subscribe(OrderEventType.ORDER_CANCELLED, stockService);

        System.out.println("• Disparando Evento: PAYMENT_CONFIRMED para Pedido #9001:");
        eventManager.notify(OrderEventType.PAYMENT_CONFIRMED, "9001", "Pagamento de R$ 350.00 confirmado via PIX.");

        System.out.println("\n• Disparando Evento: ORDER_CANCELLED para Pedido #9002:");
        eventManager.notify(OrderEventType.ORDER_CANCELLED, "9002", "Cancelado pelo cliente por desistência.");
    }

    private static void demonstrateFacade() {
        System.out.println("\n--------------------------------------------------------------------------------");
        System.out.println(" 5. PADRÃO ESTRUTURAL: FACADE (Checkout Unificado Integrando Todos os Padrões)");
        System.out.println("--------------------------------------------------------------------------------");

        OrderCheckoutFacade checkoutFacade = new OrderCheckoutFacade();

        // Cenário 1: Checkout com Frete Expresso e Cartão de Crédito
        OrderCheckoutFacade.CheckoutSummary summary1 = checkoutFacade.checkout(
                "CUST-1001",
                new BigDecimal("499.90"),
                new ExpressShipping(),
                3.2,
                85.0,
                PaymentMethod.CREDIT_CARD
        );

        System.out.printf("%n  [Resultado 1] Pedido: %s | Sucesso: %s | Total: R$ %.2f | Rastreio: %s%n",
                summary1.orderId(), summary1.success(), summary1.totalAmount(), summary1.trackingCode());

        // Cenário 2: Checkout com Retirada em Loja e PIX
        OrderCheckoutFacade.CheckoutSummary summary2 = checkoutFacade.checkout(
                "CUST-1002",
                new BigDecimal("120.00"),
                new InStorePickupShipping(),
                1.0,
                0.0,
                PaymentMethod.PIX
        );

        System.out.printf("%n  [Resultado 2] Pedido: %s | Sucesso: %s | Total: R$ %.2f | Rastreio: %s%n",
                summary2.orderId(), summary2.success(), summary2.totalAmount(), summary2.trackingCode());
    }

    private static void printFooter() {
        System.out.println("\n================================================================================");
        System.out.println("             EXECUÇÃO FINALIZADA: TODOS OS PADRÕES FORAM DEMONSTRADOS!           ");
        System.out.println("================================================================================\n");
    }
}
