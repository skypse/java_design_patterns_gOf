package com.gfb.designpatterns.creational.factory;

public class EmailNotification implements Notification {

    @Override
    public void send(String recipient, String message) {
        System.out.printf("  [EMAIL] Enviando para <%s>: \"%s\"%n", recipient, message);
    }

    @Override
    public String getChannelName() {
        return "E-mail";
    }
}
