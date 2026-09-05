package com.gfb.designpatterns.behavioral.strategy;

import java.math.BigDecimal;

/**
 * Estratégia concreta: Retirada em Loja (Click & Collect).
 * Custo zero de frete, retirada disponível em poucas horas ou no dia seguinte.
 */
public class InStorePickupShipping implements ShippingStrategy {

    @Override
    public BigDecimal calculate(double weightInKg, double distanceInKm) {
        return BigDecimal.ZERO.setScale(2);
    }

    @Override
    public String getServiceDescription() {
        return "Retirada na Loja Física (Sem Custo de Envio)";
    }

    @Override
    public int getDeliveryDaysEstimate() {
        return 0; // Disponível para retirada imediata
    }
}
