package com.gfb.designpatterns.structural.facade.subsystems;

import java.math.BigDecimal;
import java.util.UUID;

public class PaymentGateway {

    public enum PaymentMethod {
        PIX, CREDIT_CARD, BOLETO
    }

    public record PaymentResult(boolean success, String transactionId, String message) {}

    public PaymentResult processPayment(String customerId, BigDecimal amount, PaymentMethod method) {
        System.out.printf("  [Subsistema Pagamento] Processando R$ %.2f via %s para o cliente %s...%n", amount, method, customerId);

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return new PaymentResult(false, null, "Valor de transação inválido.");
        }

        String transactionId = "TX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return new PaymentResult(true, transactionId, "Pagamento aprovado com sucesso.");
    }
}
