package model.topic

import model.Alert
import model.user.AlertObserver
import model.user.UserObserver

interface AlertObservable {
    fun addObserver(observer: AlertObserver): Any
    fun removeObserver(observer: AlertObserver): Any
    fun notifyObservers(alert: Alert): Any
    fun notifyObserver(alert: Alert, subscriber: UserObserver): Any
}