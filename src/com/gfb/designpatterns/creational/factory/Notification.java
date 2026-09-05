package com.gfb.designpatterns.creational.factory;

/**
 * Interface Produto do padrão Factory Method.
 * Define o contrato comum para todos os tipos de canais de notificação.
 */
public interface Notification {
    void send(String recipient, String message);
    String getChannelName();
}
