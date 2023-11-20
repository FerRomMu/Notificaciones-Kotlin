package model.user

import model.alert.Alert

data class UserObserver(val user: User): AlertObserver {
    override fun update(alert: Alert): Any = user.addNotification(alert)
}