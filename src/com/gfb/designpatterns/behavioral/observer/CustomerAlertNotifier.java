package com.gfb.designpatterns.behavioral.observer;

public class CustomerAlertNotifier implements OrderEventListener {

    private final String customerEmail;

    public CustomerAlertNotifier(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    @Override
    public void onOrderEvent(OrderEventType eventType, String orderId, String details) {
        System.out.printf("    -> [OBSERVER: Cliente (%s)] Pedido #%s atualizado: [%s] - %s%n",
                customerEmail, orderId, eventType, details);
    }

    @Override
    public String getListenerName() {
        return "CustomerAlertNotifier (" + customerEmail + ")";
    }
}
