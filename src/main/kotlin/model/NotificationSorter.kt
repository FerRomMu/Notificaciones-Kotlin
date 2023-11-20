package model

import model.alert.AlertPriority
import model.notification.Notification
import model.topic.Receiver
import java.util.*

class NotificationSorter<T : Notification>() {

    private val urgentNotifications: Stack<T> = Stack()
    private val informativeNotifications: MutableList<T> = mutableListOf()

    fun add(notification: T) =
        when (notification.alert.alertPriority) {
            AlertPriority.URGENTE -> urgentNotifications.push(notification)
            AlertPriority.INFORMATIVA -> informativeNotifications.add(notification)
        }

    fun get(): MutableList<T> = (urgentNotifications.toList() + informativeNotifications).toMutableList()
}