package model

class AlertSystem {

    val registry: HashSet<User> = HashSet()

    fun register(user: User) = registry.add(user)

    fun isRegitered(user: User): Boolean = registry.contains(user)
}