package com.gfb.designpatterns.structural.facade.subsystems;

import java.util.UUID;

public class LogisticsService {

    public record DispatchInfo(String trackingCode, String originHub, String destinationCity) {}

    public DispatchInfo dispatchOrder(String orderId, String destinationCep, String destinationCity) {
        System.out.printf("  [Subsistema Logística] Gerando etiqueta de envio para Pedido #%s (Destino: %s - CEP %s)...%n",
                orderId, destinationCity, destinationCep);

        String trackingCode = "BR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return new DispatchInfo(trackingCode, "Centro de Distribuição Principal - SP", destinationCity);
    }
}
