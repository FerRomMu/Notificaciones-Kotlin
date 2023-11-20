package model.topic

import model.alert.Alert

data class TopicNotification(
    val alert: Alert,
    val sendTo: Receiver
) {
    fun isExpired() = alert.isExpired()
}