package com.gfb.designpatterns.creational.factory;

public class PushNotification implements Notification {

    @Override
    public void send(String recipient, String message) {
        System.out.printf("  [PUSH] Disparando alerta Push para o dispositivo <%s>: \"%s\"%n", recipient, message);
    }

    @Override
    public String getChannelName() {
        return "Push Notification";
    }
}
