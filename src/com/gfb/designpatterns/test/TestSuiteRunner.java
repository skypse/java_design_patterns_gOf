package com.gfb.designpatterns.test;

import com.gfb.designpatterns.behavioral.observer.*;
import com.gfb.designpatterns.behavioral.strategy.*;
import com.gfb.designpatterns.creational.factory.*;
import com.gfb.designpatterns.creational.singleton.*;
import com.gfb.designpatterns.structural.facade.OrderCheckoutFacade;
import com.gfb.designpatterns.structural.facade.subsystems.PaymentGateway.PaymentMethod;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Suíte de Testes Automatizados em Java Puro.
 * Valida de forma rigorosa as invariantes, comportamento e regras de cada padrão de projeto.
 */
public class TestSuiteRunner {

    private static int totalTests = 0;
    private static int passedTests = 0;
    private static int failedTests = 0;

    public static void main(String[] args) {
        System.out.println("================================================================================");
        System.out.println("            INICIANDO SUÍTE DE TESTES AUTOMATIZADOS (JAVA PURO)                 ");
        System.out.println("================================================================================");

        testSingletonLazyHolder();
        testSingletonEnum();
        testFactoryMethod();
        testStrategyPattern();
        testObserverPattern();
        testFacadePattern();

        printSummary();
    }

    private static void assertTrue(String testName, boolean condition, String errorMessage) {
        totalTests++;
        if (condition) {
            passedTests++;
            System.out.printf("  [PASS] %s%n", testName);
        } else {
            failedTests++;
            System.err.printf("  [FAIL] %s - Motivo: %s%n", testName, errorMessage);
        }
    }

    private static void assertEquals(String testName, Object expected, Object actual) {
        totalTests++;
        if (expected == null && actual == null) {
            passedTests++;
            System.out.printf("  [PASS] %s%n", testName);
            return;
        }

        if (expected != null && expected.equals(actual)) {
            passedTests++;
            System.out.printf("  [PASS] %s%n", testName);
        } else {
            failedTests++;
            System.err.printf("  [FAIL] %s - Esperado: <%s>, Obtido: <%s>%n", testName, expected, actual);
        }
    }

    // 1. TESTES DO SINGLETON
    private static void testSingletonLazyHolder() {
        System.out.println("\n• Testando Singleton (ConfigurationManager)...");
        ConfigurationManager instance1 = ConfigurationManager.getInstance();
        ConfigurationManager instance2 = ConfigurationManager.getInstance();

        assertTrue("Mesma referência de memória entre chamadas de getInstance", instance1 == instance2, "Instâncias diferentes foram retornadas");
        assertEquals("Configuração padrão app.version presente", "1.0.0", instance1.getProperty("app.version"));

        instance1.setProperty("test.key", "test.value");
        assertEquals("Mutação refletida imediatamente na outra referência", "test.value", instance2.getProperty("test.key"));
    }

    private static void testSingletonEnum() {
        System.out.println("\n• Testando Singleton Enum (AppTheme)...");
        AppTheme theme1 = AppTheme.DARK_MODE;
        AppTheme theme2 = AppTheme.valueOf("DARK_MODE");

        assertTrue("Enum Singleton garante instância única pela JVM", theme1 == theme2, "Instâncias de enum divergentes");
        assertEquals("Cor de fundo correta para DARK_MODE", "#1E1E2E", theme1.getBackgroundColor());
    }

    // 2. TESTES DO FACTORY METHOD
    private static void testFactoryMethod() {
        System.out.println("\n• Testando Factory Method (NotificationFactory)...");

        Notification email = NotificationFactory.createNotification(NotificationFactory.NotificationType.EMAIL);
        assertTrue("Instância retornada é EmailNotification", email instanceof EmailNotification, "Esperado EmailNotification");
        assertEquals("Nome de canal Email", "E-mail", email.getChannelName());

        Notification sms = NotificationFactory.createNotification(NotificationFactory.NotificationType.SMS);
        assertTrue("Instância retornada é SmsNotification", sms instanceof SmsNotification, "Esperado SmsNotification");
        assertEquals("Nome de canal SMS", "SMS", sms.getChannelName());

        Notification push = NotificationFactory.createNotification(NotificationFactory.NotificationType.PUSH);
        assertTrue("Instância retornada é PushNotification", push instanceof PushNotification, "Esperado PushNotification");
        assertEquals("Nome de canal Push Notification", "Push Notification", push.getChannelName());

        // Teste de exceção para tipo nulo
        boolean exceptionThrown = false;
        try {
            NotificationFactory.createNotification(null);
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }
        assertTrue("Lança IllegalArgumentException ao passar tipo nulo", exceptionThrown, "Nenhuma exceção lançada");
    }

    // 3. TESTES DO STRATEGY
    private static void testStrategyPattern() {
        System.out.println("\n• Testando Strategy (Shipping Calculator & Algoritmos)...");

        double weight = 2.0; // kg
        double distance = 100.0; // km

        // Retirada em loja: sempre 0.00
        ShippingCalculator calculator = new ShippingCalculator(new InStorePickupShipping());
        BigDecimal storePickupCost = calculator.calculateShipping(weight, distance);
        assertEquals("Retirada em loja custo R$ 0.00", new BigDecimal("0.00"), storePickupCost);
        assertEquals("Prazo de retirada em loja zero dias", 0, calculator.getStrategy().getDeliveryDaysEstimate());

        // Frete Econômico: 10 + (1.80 * 2) + (0.12 * 100) = 10 + 3.60 + 12.00 = 25.60
        calculator.setStrategy(new EconomyShipping());
        BigDecimal economyCost = calculator.calculateShipping(weight, distance);
        assertEquals("Cálculo correto Frete Econômico", new BigDecimal("25.60"), economyCost);
        assertEquals("Prazo de frete econômico 5 dias", 5, calculator.getStrategy().getDeliveryDaysEstimate());

        // Frete Expresso: 25 + (4.50 * 2) + (0.35 * 100) = 25 + 9.00 + 35.00 = 69.00
        calculator.setStrategy(new ExpressShipping());
        BigDecimal expressCost = calculator.calculateShipping(weight, distance);
        assertEquals("Cálculo correto Frete Expresso", new BigDecimal("69.00"), expressCost);
        assertEquals("Prazo de frete expresso 1 dia", 1, calculator.getStrategy().getDeliveryDaysEstimate());
    }

    // 4. TESTES DO OBSERVER
    private static void testObserverPattern() {
        System.out.println("\n• Testando Observer (OrderEventManager & Listeners)...");

        OrderEventManager manager = new OrderEventManager();
        List<String> receivedEvents = new ArrayList<>();

        OrderEventListener mockListener = new OrderEventListener() {
            @Override
            public void onOrderEvent(OrderEventType eventType, String orderId, String details) {
                receivedEvents.add(eventType.name() + ":" + orderId);
            }

            @Override
            public String getListenerName() {
                return "MockListener";
            }
        };

        manager.subscribe(OrderEventType.PAYMENT_CONFIRMED, mockListener);
        manager.notify(OrderEventType.PAYMENT_CONFIRMED, "TEST-100", "Aprovado");

        assertEquals("Ouvinte recebeu evento inscrito", 1, receivedEvents.size());
        assertEquals("Conteúdo do evento recebido correto", "PAYMENT_CONFIRMED:TEST-100", receivedEvents.get(0));

        // Evento que o ouvinte não se inscreveu não deve ser entregue
        manager.notify(OrderEventType.ORDER_CANCELLED, "TEST-100", "Cancelado");
        assertEquals("Ouvinte não recebe eventos não subscritos", 1, receivedEvents.size());

        // Após unsubscribe, não recebe mais notificações
        manager.unsubscribe(OrderEventType.PAYMENT_CONFIRMED, mockListener);
        manager.notify(OrderEventType.PAYMENT_CONFIRMED, "TEST-200", "Outro");
        assertEquals("Ouvinte desinscrito não recebe novas mensagens", 1, receivedEvents.size());
    }

    // 5. TESTES DO FACADE
    private static void testFacadePattern() {
        System.out.println("\n• Testando Facade (OrderCheckoutFacade)...");

        OrderCheckoutFacade facade = new OrderCheckoutFacade();

        // 1. Sucesso com Frete Econômico: 200 + (10 + 1.8*1 + 0.12*50 = 10 + 1.8 + 6 = 17.80) = 217.80
        OrderCheckoutFacade.CheckoutSummary summarySuccess = facade.checkout(
                "CUST-001",
                new BigDecimal("200.00"),
                new EconomyShipping(),
                1.0,
                50.0,
                PaymentMethod.PIX
        );

        assertTrue("Checkout aprovado com sucesso", summarySuccess.success(), "Esperado checkout bem sucedido");
        assertTrue("Código de rastreio gerado", summarySuccess.trackingCode() != null && summarySuccess.trackingCode().startsWith("BR-"), "Rastreio inválido");
        assertEquals("Valor total com frete bate exatamente", new BigDecimal("217.80"), summarySuccess.totalAmount());

        // 2. Falha com valor zerado/negativo de produtos e frete grátis (0.00)
        OrderCheckoutFacade.CheckoutSummary summaryFail = facade.checkout(
                "CUST-002",
                BigDecimal.ZERO,
                new InStorePickupShipping(),
                0.0,
                0.0,
                PaymentMethod.CREDIT_CARD
        );

        assertTrue("Checkout rejeitado com valor total zero", !summaryFail.success(), "Esperado checkout reprovado por valor inválido");
    }

    private static void printSummary() {
        System.out.println("\n================================================================================");
        System.out.println("                        RESUMO DOS RESULTADOS                                   ");
        System.out.println("================================================================================");
        System.out.printf("Total de Testes Executados: %d%n", totalTests);
        System.out.printf("Testes Aprovados:          %d%n", passedTests);
        System.out.printf("Testes Reprovados:         %d%n", failedTests);
        System.out.println("================================================================================");

        if (failedTests > 0) {
            System.err.println("❌ STATUS: FALHA! Alguns testes não passaram.");
            System.exit(1);
        } else {
            System.out.println("✅ STATUS: SUCESSO ABSOLUTO! Todos os padrões estão 100% íntegros e funcionais.");
        }
    }
}
