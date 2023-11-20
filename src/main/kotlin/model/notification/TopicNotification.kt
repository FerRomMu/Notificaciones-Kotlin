package model.notification

import model.alert.Alert
import model.topic.Receiver

data class TopicNotification(
    override val alert: Alert,
    val sendTo: Receiver
): Notification {
    fun isExpired() = alert.isExpired()
}