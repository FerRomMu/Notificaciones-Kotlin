package model.user

import exceptions.NonExistentNotificationException
import model.alert.Alert

data class User(val userName: String,
    private val notifications: MutableList<Notification> = mutableListOf()) {

    fun notification(alert: Alert) =
        notifications
            .find { notification -> alert == notification.alert }
            ?: throw NonExistentNotificationException()

    fun addNotification(alert: Alert) = notifications.add(Notification(alert))

    fun isReaden(alert: Alert): Boolean = notification(alert).isReaden

    fun markAsRead(alert: Alert) = notification(alert).read()

    fun hasNotification(alert: Alert): Boolean =
        notifications.contains(Notification(alert))

    fun getNotifications(): List<Alert> =
        notifications
            .filter { notification ->
                !notification.isExpired() && !notification.isReaden }
            .map { notification -> notification.alert }
}