package com.kretov.accountmanagement.view;

import com.vaadin.flow.component.notification.Notification;

class Util {
    private Util() {
    }

    static void showNotification(String message) {
        Notification notification = new Notification(message, 3000);
        notification.open();
    }
}
