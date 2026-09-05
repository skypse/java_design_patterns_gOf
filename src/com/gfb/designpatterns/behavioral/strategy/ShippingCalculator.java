package com.gfb.designpatterns.behavioral.strategy;

import java.math.BigDecimal;

/**
 * Classe Contexto no padrão Strategy.
 * Mantém uma referência a uma instância de ShippingStrategy e delega o cálculo
 * a ela, permitindo a troca dinâmica do algoritmo em tempo de execução.
 */
public class ShippingCalculator {

    private ShippingStrategy strategy;

    public ShippingCalculator(ShippingStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(ShippingStrategy strategy) {
        this.strategy = strategy;
    }

    public ShippingStrategy getStrategy() {
        return strategy;
    }

    public BigDecimal calculateShipping(double weightInKg, double distanceInKm) {
        if (strategy == null) {
            throw new IllegalStateException("Nenhuma estratégia de frete foi configurada.");
        }
        return strategy.calculate(weightInKg, distanceInKm);
    }
}
