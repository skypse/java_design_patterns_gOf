package com.gfb.designpatterns.behavioral.observer;

public class InventoryStockManager implements OrderEventListener {

    @Override
    public void onOrderEvent(OrderEventType eventType, String orderId, String details) {
        if (eventType == OrderEventType.PAYMENT_CONFIRMED) {
            System.out.printf("    -> [OBSERVER: Estoque] Pedido #%s aprovado. Reservando itens e gerando ordem de separação no armazém.%n", orderId);
        } else if (eventType == OrderEventType.ORDER_CANCELLED) {
            System.out.printf("    -> [OBSERVER: Estoque] Pedido #%s cancelado. Estornando itens de volta ao estoque disponível.%n", orderId);
        }
    }

    @Override
    public String getListenerName() {
        return "InventoryStockManager";
    }
}
