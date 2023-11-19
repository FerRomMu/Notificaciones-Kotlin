package model

import exceptions.DuplicateUsernameException
import exceptions.ExistingTopicException
import exceptions.NonExistentTopicException

class AlertSystem {

    private val registry: HashSet<User> = HashSet()
    private val topicsSuscriptions: HashMap<Topic, HashSet<User>> = HashMap()

    fun register(user: User) {
        val isNewUsername = registry.add(user)
        if (!isNewUsername) {
            throw DuplicateUsernameException()
        }
    }
    fun isRegitered(user: User): Boolean = registry.contains(user)

    fun registerTopic(topic: Topic) {
        if (isTopicAlert(topic)) { throw ExistingTopicException() }
        topicsSuscriptions[topic] = HashSet(registry)
    }
    fun isTopicAlert(topic: Topic): Boolean = topicsSuscriptions.contains(topic)

    fun subscribeUserTo(user: User, topicFollowed: Topic) =
        topicSuscribers(topicFollowed).add(user)
    fun unsubscribeUserTo(user: User, topicUnfollowed: Topic) =
        topicSuscribers(topicUnfollowed).remove(user)

    private fun topicSuscribers(topicFollowed: Topic) =
        topicsSuscriptions[topicFollowed] ?: throw NonExistentTopicException()

    fun isSubscribedTo(user: User, topic: Topic): Boolean =
        topicsSuscriptions[topic]!!.contains(user)

    fun sendAlert(alert: Alert) {
        topicSuscribers(alert.topic).map { user -> user.notifications.add(alert) }
    }

}