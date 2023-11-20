package model.user

import model.Alert

data class UserObserver(val user: User): AlertObserver {
    override fun update(alert: Alert): Any = user.notifications.add(alert)
}