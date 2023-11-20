package model.notification

import model.alert.AlertPriority
import model.topic.Receiver
import java.util.*

class NotificationSorter<T : Notification>() {

    private val urgentNotifications: LinkedList<T> = LinkedList()
    private val informativeNotifications: MutableList<T> = mutableListOf()

    fun add(notification: T) =
        when (notification.alert.alertPriority) {
            AlertPriority.URGENTE -> urgentNotifications.addFirst(notification)
            AlertPriority.INFORMATIVA -> informativeNotifications.add(notification)
        }

    fun get(): MutableList<T> = (urgentNotifications.toList() + informativeNotifications).toMutableList()
}