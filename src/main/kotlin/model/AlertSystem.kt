package model

class AlertSystem {

    val registry: HashSet<User> = HashSet()
    val topicsSuscriptions: HashMap<Topic, HashSet<User>> = HashMap()

    fun register(user: User) = registry.add(user)
    fun isRegitered(user: User): Boolean = registry.contains(user)

    fun registerTopic(topic: Topic) = topicsSuscriptions.set(topic, HashSet(registry))
    fun isTopicAlert(topic: Topic): Boolean = topicsSuscriptions.contains(topic)

    fun subscribeUserTo(user: User, topicFollowed: Topic) {
        val suscribers = topicsSuscriptions[topicFollowed]
        suscribers!!.add(user)
        topicsSuscriptions[topicFollowed] = suscribers
    }
    fun unsubscribeUserTo(user: User, topicUnfollowed: Topic) {
        val suscribers = topicsSuscriptions[topicUnfollowed]
        suscribers!!.remove(user)
        topicsSuscriptions[topicUnfollowed] = suscribers
    }
    fun isSubscribedTo(user: User, topic: Topic): Boolean =
        topicsSuscriptions[topic]!!.contains(user)

}