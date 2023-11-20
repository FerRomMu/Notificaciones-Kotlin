package model.topic

import exceptions.NonExistentSubscriberException
import model.NotificationSorter
import model.alert.Alert
import model.notification.Notification
import model.notification.TopicNotification
import model.user.AlertObserver
import model.user.UserObserver
import java.util.*
import kotlin.collections.HashSet

class TopicObservable(val subscribers: HashSet<AlertObserver>) : AlertObservable {

    private val notifications: NotificationSorter<TopicNotification> = NotificationSorter()

    override fun addObserver(observer: AlertObserver) =
        subscribers.add(observer)

    override fun removeObserver(observer: AlertObserver) =
        subscribers.remove(observer)

    override fun notifyObservers(alert: Alert) {
        notifications.add(TopicNotification(alert, Receiver.ALL_USERS))
        subscribers.map { subcriber -> subcriber.update(alert) }
    }

    override fun notifyObserver(alert: Alert, subscriber: UserObserver) {
        if (!subscribers.contains(subscriber)) { throw NonExistentSubscriberException() }
        notifications.add(TopicNotification(alert, Receiver.SINGLE_USER))
        subscriber.update(alert)
    }

    fun isObserver(userObserver: UserObserver): Boolean =
        subscribers.contains(userObserver)

    fun getNotifications(): List<TopicNotification> =
        notifications
            .get()
            .filter { notification -> !notification.isExpired() }

}