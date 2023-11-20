package model.topic

import model.Alert
import model.user.AlertObserver

interface AlertObservable {
    fun addObserver(observer: AlertObserver): Any
    fun removeObserver(observer: AlertObserver): Any
    fun notifyObservers(alert: Alert): Any
}