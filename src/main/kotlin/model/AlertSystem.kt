package model

import exceptions.DuplicateUsernameException
import exceptions.ExistingTopicException
import exceptions.NonExistentTopicException
import model.topic.Topic
import model.topic.TopicObservable
import model.user.User
import model.user.UserObserver

class AlertSystem {

    private val users: HashMap<User, UserObserver> = HashMap()
    private val topicsObservables: HashMap<Topic, TopicObservable> = HashMap()

    private fun topicObservable(topicFollowed: Topic) =
        topicsObservables[topicFollowed] ?: throw NonExistentTopicException()
    private fun userObserver(user: User) =
        users[user] ?: throw NonExistentTopicException()

    fun register(user: User) {
        if (isRegitered(user)) { throw DuplicateUsernameException() }
        users[user] = UserObserver(user)
    }
    fun isRegitered(user: User): Boolean = users.contains(user)

    fun registerTopic(topic: Topic) {
        if (isTopicAlert(topic)) { throw ExistingTopicException() }
        topicsObservables[topic] = TopicObservable(users.values.toHashSet())
    }
    fun isTopicAlert(topic: Topic): Boolean = topicsObservables.contains(topic)

    fun subscribeUserTo(user: User, topicFollowed: Topic) =
        topicObservable(topicFollowed)
            .addObserver(userObserver(user))
    fun unsubscribeUserTo(user: User, topicUnfollowed: Topic) =
        topicObservable(topicUnfollowed)
            .removeObserver(userObserver(user))
    fun isSubscribedTo(user: User, topic: Topic): Boolean =
        topicObservable(topic).isObserver(userObserver(user))

    fun sendAlert(alert: Alert) {
        topicObservable(alert.topic).notifyObservers(alert)
    }

}