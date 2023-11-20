package model.user

import model.alert.Alert

data class User(val userName: String,
    val notifications: MutableList<Alert> = mutableListOf())