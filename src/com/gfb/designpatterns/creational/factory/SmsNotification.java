package com.gfb.designpatterns.creational.factory;

public class SmsNotification implements Notification {

    @Override
    public void send(String recipient, String message) {
        System.out.printf("  [SMS] Enviando para telefone <%s>: \"%s\"%n", recipient, message);
    }

    @Override
    public String getChannelName() {
        return "SMS";
    }
}
