package model.notification

import model.alert.Alert

data class UserNotification(
    override val alert: Alert, var isReaden: Boolean = false
): Notification  {

    fun read() {
        isReaden = true
    }

    fun isExpired(): Boolean = alert.isExpired()
}