package model.notification

import model.alert.Alert
import model.alert.AlertPriority
import model.topic.Receiver
import model.topic.Topic
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.LocalDateTime

class UserNotificationTest {

    lateinit var topic: Topic
    lateinit var notification: UserNotification
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
        notification = UserNotification(
            Alert(1, topic, AlertPriority.INFORMATIVA, oldDate))

        assertTrue(notification.isExpired())
    }

    @Test
    fun `La notificacion de un tema no expira si su alerta tiene fecha de expiracion futura`(){
        notification =  UserNotification(
            Alert(1, topic, AlertPriority.INFORMATIVA, futureDate))

        assertFalse(notification.isExpired())
    }

    @Test
    fun `La notificacion de un tema no expira si su alerta no tiene fecha de expiracion`(){
        notification =  UserNotification(
            Alert(1, topic, AlertPriority.INFORMATIVA))

        assertFalse(notification.isExpired())
    }

    @Test
    fun `Por defecto la notificacion no esta leida`(){
        notification =  UserNotification(
            Alert(1, topic, AlertPriority.INFORMATIVA))

        assertFalse(notification.isReaden)
    }

    @Test
    fun `Al leer una notificacion figura como leida`(){
        notification =  UserNotification(
            Alert(1, topic, AlertPriority.INFORMATIVA))

        notification.read()

        assertTrue(notification.isReaden)
    }
}