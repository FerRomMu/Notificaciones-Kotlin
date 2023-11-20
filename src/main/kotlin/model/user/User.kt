package model.user

import model.Alert

data class User(val userName: String,
    val notifications: MutableList<Alert> = mutableListOf())