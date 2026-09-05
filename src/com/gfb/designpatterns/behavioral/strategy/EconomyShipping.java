package com.gfb.designpatterns.behavioral.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Estratégia concreta: Frete Econômico (PAC / Rodoviário convencional).
 * Custo mais baixo, prazo de entrega maior.
 */
public class EconomyShipping implements ShippingStrategy {

    private static final BigDecimal BASE_FEE = new BigDecimal("10.00");
    private static final BigDecimal COST_PER_KG = new BigDecimal("1.80");
    private static final BigDecimal COST_PER_KM = new BigDecimal("0.12");

    @Override
    public BigDecimal calculate(double weightInKg, double distanceInKm) {
        BigDecimal weightCost = COST_PER_KG.multiply(BigDecimal.valueOf(weightInKg));
        BigDecimal distanceCost = COST_PER_KM.multiply(BigDecimal.valueOf(distanceInKm));
        return BASE_FEE.add(weightCost).add(distanceCost).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String getServiceDescription() {
        return "Frete Econômico (Padrão Rodoviário)";
    }

    @Override
    public int getDeliveryDaysEstimate() {
        return 5;
    }
}
