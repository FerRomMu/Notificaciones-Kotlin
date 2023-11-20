package model.user

import model.Alert

interface AlertObserver {
    fun update(alert: Alert): Any
}