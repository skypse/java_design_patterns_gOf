package com.gfb.designpatterns.creational.factory;

/**
 * Fábrica (Creator) do padrão Factory Method.
 * Encapsula a lógica de instanciação das notificações, desacoplando o cliente
 * das implementações concretas.
 */
public class NotificationFactory {

    public enum NotificationType {
        EMAIL, SMS, PUSH
    }

    public static Notification createNotification(NotificationType type) {
        if (type == null) {
            throw new IllegalArgumentException("Tipo de notificação não pode ser nulo.");
        }

        return switch (type) {
            case EMAIL -> new EmailNotification();
            case SMS -> new SmsNotification();
            case PUSH -> new PushNotification();
        };
    }
}
