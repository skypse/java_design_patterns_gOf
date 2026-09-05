package com.gfb.designpatterns.behavioral.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Estratégia concreta: Frete Expresso.
 * Prioridade rápida, maior custo.
 */
public class ExpressShipping implements ShippingStrategy {

    private static final BigDecimal BASE_FEE = new BigDecimal("25.00");
    private static final BigDecimal COST_PER_KG = new BigDecimal("4.50");
    private static final BigDecimal COST_PER_KM = new BigDecimal("0.35");

    @Override
    public BigDecimal calculate(double weightInKg, double distanceInKm) {
        BigDecimal weightCost = COST_PER_KG.multiply(BigDecimal.valueOf(weightInKg));
        BigDecimal distanceCost = COST_PER_KM.multiply(BigDecimal.valueOf(distanceInKm));
        return BASE_FEE.add(weightCost).add(distanceCost).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String getServiceDescription() {
        return "Frete Expresso (Entrega Aérea / Rápida)";
    }

    @Override
    public int getDeliveryDaysEstimate() {
        return 1;
    }
}
