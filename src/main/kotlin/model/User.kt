package model

data class User(val userName: String,
    val notifications: MutableList<Alert> = mutableListOf())