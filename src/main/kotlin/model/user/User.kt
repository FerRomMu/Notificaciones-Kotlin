package model.user

import exceptions.NonExistentNotificationException
import model.notification.NotificationSorter
import model.alert.Alert
import model.notification.UserNotification

data class User(val userName: String) {

    private val notifications: NotificationSorter<UserNotification> = NotificationSorter()

    fun notification(alert: Alert) =
        notifications
            .get()
            .find { notification -> alert == notification.alert }
            ?: throw NonExistentNotificationException()

    fun addNotification(alert: Alert) = notifications.add(UserNotification(alert))

    fun isReaden(alert: Alert): Boolean = notification(alert).isReaden

    fun markAsRead(alert: Alert) = notification(alert).read()

    fun hasNotification(alert: Alert): Boolean =
        notifications.get().contains(UserNotification(alert))

    fun getNotifications(): List<Alert> =
        notifications
            .get()
            .filter { notification ->
                !notification.isExpired() && !notification.isReaden }
            .map { notification -> notification.alert }
}