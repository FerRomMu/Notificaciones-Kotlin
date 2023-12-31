package model.alert

import model.topic.Topic
import java.time.LocalDateTime

data class Alert(val id: Int,
                 val topic: Topic,
                 val alertPriority: AlertPriority,
                 val expirationDate: LocalDateTime? = null) {

    fun isExpired(): Boolean = expirationDate != null && expirationDate < LocalDateTime.now()
}
