package model.topic

import exceptions.NonExistentSubscriberException
import model.Alert
import model.user.AlertObserver
import model.user.User
import model.user.UserObserver

class TopicObservable(val subscribers: HashSet<AlertObserver>) : AlertObservable {

    override fun addObserver(observer: AlertObserver) =
        subscribers.add(observer)

    override fun removeObserver(observer: AlertObserver) =
        subscribers.remove(observer)

    override fun notifyObservers(alert: Alert) =
        subscribers.map { subcriber -> subcriber.update(alert) }

    override fun notifyObserver(alert: Alert, subscriber: UserObserver) {
        if (!subscribers.contains(subscriber)) { throw NonExistentSubscriberException() }
        subscriber.update(alert)
    }

    fun isObserver(userObserver: UserObserver): Boolean =
        subscribers.contains(userObserver)
}