package model.user

import model.alert.Alert

interface AlertObserver {
    fun update(alert: Alert): Any
}