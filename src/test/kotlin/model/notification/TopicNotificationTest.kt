package model.notification

import model.AlertSystem
import model.alert.Alert
import model.alert.AlertPriority
import model.topic.Receiver
import model.topic.Topic
import model.user.User
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.LocalDateTime

class TopicNotificationTest {

    lateinit var topic: Topic
    lateinit var notification: TopicNotification
    lateinit var oldDate: LocalDateTime
    lateinit var futureDate: LocalDateTime

    @BeforeEach
    fun setup(){
        topic = Topic("TemaA")
        oldDate = LocalDateTime.now().minus(Duration.ofDays(10))
        futureDate = LocalDateTime.now().plus(Duration.ofDays(10))
    }

    @Test
    fun `La notificacion de un tema expira si su alerta tiene fecha de expiracion antigua`(){
        notification = TopicNotification(
            Alert(1, topic, AlertPriority.INFORMATIVA, oldDate),
            Receiver.SINGLE_USER)

        assertTrue(notification.isExpired())
    }

    @Test
    fun `La notificacion de un tema no expira si su alerta tiene fecha de expiracion futura`(){
        notification = TopicNotification(
            Alert(1, topic, AlertPriority.INFORMATIVA, futureDate),
            Receiver.SINGLE_USER)

        assertFalse(notification.isExpired())
    }

    @Test
    fun `La notificacion de un tema no expira si su alerta no tiene fecha de expiracion`(){
        notification = TopicNotification(
            Alert(1, topic, AlertPriority.INFORMATIVA),
            Receiver.SINGLE_USER)

        assertFalse(notification.isExpired())
    }
}