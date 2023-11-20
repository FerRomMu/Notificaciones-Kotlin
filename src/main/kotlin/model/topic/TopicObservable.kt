package model.topic

import exceptions.NonExistentSubscriberException
import model.alert.Alert
import model.user.AlertObserver
import model.user.UserObserver

class TopicObservable(val subscribers: HashSet<AlertObserver>) : AlertObservable {

    private val topicNotifications: MutableList<TopicNotification> = mutableListOf()

    override fun addObserver(observer: AlertObserver) =
        subscribers.add(observer)

    override fun removeObserver(observer: AlertObserver) =
        subscribers.remove(observer)

    override fun notifyObservers(alert: Alert) {
        topicNotifications.add(TopicNotification(alert, Receiver.ALL_USERS))
        subscribers.map { subcriber -> subcriber.update(alert) }
    }

    override fun notifyObserver(alert: Alert, subscriber: UserObserver) {
        if (!subscribers.contains(subscriber)) { throw NonExistentSubscriberException() }
        topicNotifications.add(TopicNotification(alert, Receiver.SINGLE_USER))
        subscriber.update(alert)
    }

    fun isObserver(userObserver: UserObserver): Boolean =
        subscribers.contains(userObserver)

    fun getAlerts(): List<Alert> =
        topicNotifications
            .filter { notification -> !notification.isExpired() }
            .map { notification -> notification.alert }

}