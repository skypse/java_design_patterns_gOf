package com.gfb.designpatterns.behavioral.observer;

/**
 * Interface Observer (Listener).
 * Os assinantes interessados em eventos de pedidos implementam esta interface.
 */
public interface OrderEventListener {
    void onOrderEvent(OrderEventType eventType, String orderId, String details);
    String getListenerName();
}
