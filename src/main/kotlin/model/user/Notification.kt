package model.user

import model.alert.Alert

data class Notification(val alert: Alert, var isReaden: Boolean = false) {

    fun read() {
        isReaden = true
    }
}