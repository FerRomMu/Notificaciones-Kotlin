package model.user

import model.alert.Alert
import java.time.LocalDateTime

data class Notification(val alert: Alert, var isReaden: Boolean = false) {

    fun read() {
        isReaden = true
    }

    fun isExpired(): Boolean = alert.isExpired()
}