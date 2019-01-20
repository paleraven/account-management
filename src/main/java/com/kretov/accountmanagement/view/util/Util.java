package com.kretov.accountmanagement.view.util;

import com.vaadin.flow.component.notification.Notification;

public class Util {
    private Util() {
    }

    public static void showNotification(String message) {
        Notification notification = new Notification(message, 3000);
        notification.open();
    }
}
