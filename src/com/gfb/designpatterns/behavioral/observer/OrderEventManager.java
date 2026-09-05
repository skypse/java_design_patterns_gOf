package com.gfb.designpatterns.behavioral.observer;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Subject (Publicador) no padrão Observer.
 * Permite que observadores se inscrevam ou cancelem a inscrição em eventos específicos.
 */
public class OrderEventManager {

    private final Map<OrderEventType, List<OrderEventListener>> listeners = new EnumMap<>(OrderEventType.class);

    public OrderEventManager() {
        for (OrderEventType type : OrderEventType.values()) {
            listeners.put(type, new ArrayList<>());
        }
    }

    public void subscribe(OrderEventType eventType, OrderEventListener listener) {
        List<OrderEventListener> users = listeners.get(eventType);
        if (!users.contains(listener)) {
            users.add(listener);
        }
    }

    public void unsubscribe(OrderEventType eventType, OrderEventListener listener) {
        List<OrderEventListener> users = listeners.get(eventType);
        users.remove(listener);
    }

    public void notify(OrderEventType eventType, String orderId, String details) {
        List<OrderEventListener> users = listeners.get(eventType);
        if (users != null) {
            for (OrderEventListener listener : users) {
                listener.onOrderEvent(eventType, orderId, details);
            }
        }
    }
}
