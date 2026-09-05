package com.gfb.designpatterns.behavioral.strategy;

import java.math.BigDecimal;

/**
 * Interface Strategy para cálculo de frete.
 * Define o algoritmo que varia independentemente dos clientes que o utilizam.
 */
public interface ShippingStrategy {
    BigDecimal calculate(double weightInKg, double distanceInKm);
    String getServiceDescription();
    int getDeliveryDaysEstimate();
}
