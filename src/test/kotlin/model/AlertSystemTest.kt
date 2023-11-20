package model
import exceptions.*
import model.alert.Alert
import model.alert.AlertPriority
import model.topic.Receiver
import model.topic.Topic
import model.notification.TopicNotification
import model.user.User
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.time.Duration
import java.time.LocalDateTime

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AlertSystemTest {

    lateinit var system: AlertSystem
    lateinit var user: User
    lateinit var topic: Topic
    lateinit var alert: Alert

    @BeforeEach
    fun setup(){
        system = AlertSystem()
        user = User("UsuarioA")
        topic = Topic("TemaA")
        alert = Alert(1, topic, AlertPriority.INFORMATIVA)
        system.register(user)
        system.registerTopic(topic)
    }

    @Test
    fun `se puede registrar un usuario para recibir alertas`(){
        val userB = User("UsuarioB")

        system.register(userB)

        assertTrue(system.isRegitered(userB))
    }

    @Test
    fun `no se puede registrar un usuario para recibir alertas con username repetido`(){
        val userB = User("UsuarioA")

        assertThrows(DuplicateUsernameException::class.java) { system.register(userB) }
    }

    @Test
    fun `se puede registrar temas  sobre los cuales se enviarán alertas`(){
        val topicB = Topic("TemaB")

        system.registerTopic(topicB)

        assertTrue(system.isTopicAlert(topicB))
    }

    @Test
    fun `no se puede registrar un tema repetido`(){
        val topicRepeated = Topic("TemaA")

        assertThrows(ExistingTopicException::class.java) { system.registerTopic(topicRepeated) }
    }

    @Test
    fun `un usuario puede elegir los temas en los que esta inscripto`(){
        val topicUnfollowed = Topic("TemaB")
        val topicFollowed = Topic("TemaC")
        system.registerTopic(topicUnfollowed)
        system.registerTopic(topicFollowed)

        system.subscribeUserTo(user, topicFollowed)
        system.unsubscribeUserTo(user, topicUnfollowed)

        assertTrue(system.isSubscribedTo(user, topicFollowed))
        assertFalse(system.isSubscribedTo(user, topicUnfollowed))
    }

    @Test
    fun `un usuario no se puede suscribir o desuscribir a un tema no registrado`(){
        val nonExistentTopic = Topic("TemaB")

        assertThrows(NonExistentTopicException::class.java) { system.subscribeUserTo(user, nonExistentTopic) }
        assertThrows(NonExistentTopicException::class.java) { system.unsubscribeUserTo(user, nonExistentTopic) }
    }

    @Test
    fun `un usuario no se puede suscribir o desuscribir a un tema si el no esta registrado`(){
        val nonExistantUser = User("userB")

        assertThrows(NonExistentUserException::class.java) { system.subscribeUserTo(nonExistantUser, topic) }
        assertThrows(NonExistentUserException::class.java) { system.unsubscribeUserTo(nonExistantUser, topic) }
    }

    @Test
    fun `si se envia a un solo usuario una alerta para un tema que no esta suscripto falla`(){
        val userA = User("UserA")
        system.register(userA)
        system.unsubscribeUserTo(userA, topic)

        assertThrows(NonExistentSubscriberException::class.java) { system.sendAlertTo(alert, userA) }
    }

    @Test
    fun `se puede obtener las alertas no expiradas de un tema`(){
        val oldDate = LocalDateTime.now().minus(Duration.ofDays(10))
        val futureDate = LocalDateTime.now().plus(Duration.ofDays(10))
        val alertNonExpired = Alert(2, topic, AlertPriority.INFORMATIVA, futureDate)
        val alertNonExpired2 = Alert(3, topic, AlertPriority.INFORMATIVA, futureDate)
        val alertExpired = Alert(4, topic, AlertPriority.INFORMATIVA, oldDate)
        system.sendAlert(alertNonExpired)
        system.sendAlert(alertNonExpired2)
        system.sendAlert(alertExpired)

        val alertsNonExpired: List<Alert> = system
            .getNotificationsFromTopic(topic)
            .map { notification -> notification.alert }

        assertTrue(alertsNonExpired.contains(alertNonExpired))
        assertTrue(alertsNonExpired.contains(alertNonExpired2))
        assertFalse(alertsNonExpired.contains(alertExpired))
    }

    @Test
    fun `se puede saber si la alerta fue grupal o a un usuario al obtener las alertas de un tema`(){
        val oldDate = LocalDateTime.now().minus(Duration.ofDays(10))
        val futureDate = LocalDateTime.now().plus(Duration.ofDays(10))
        val alertToAll = Alert(2, topic, AlertPriority.INFORMATIVA, futureDate)
        val alertToUser = Alert(3, topic, AlertPriority.INFORMATIVA, futureDate)
        val alertExpired = Alert(4, topic, AlertPriority.INFORMATIVA, oldDate)
        system.sendAlert(alertToAll)
        system.sendAlertTo(alertToUser, user)
        system.sendAlert(alertExpired)

        val notificationsNonExpired: List<TopicNotification> =
            system.getNotificationsFromTopic(topic)

        assertEquals(Receiver.ALL_USERS, notificationsNonExpired[0].sendTo)
        assertEquals(Receiver.SINGLE_USER, notificationsNonExpired[1].sendTo)
    }

    @Test
    fun `el orden de las notificaciones es LIFO para prioritas y FIFO informativas`(){
        // I1,I2,U1,I3,U2,I4
        val informativeAlert1 = Alert(2, topic, AlertPriority.INFORMATIVA)
        val informativeAlert2 = Alert(3, topic, AlertPriority.INFORMATIVA)
        val informativeAlert3 = Alert(3, topic, AlertPriority.INFORMATIVA)
        val informativeAlert4 = Alert(3, topic, AlertPriority.INFORMATIVA)
        val urgentAlert1 = Alert(4, topic, AlertPriority.URGENTE)
        val urgentAlert2 = Alert(5, topic, AlertPriority.URGENTE)
        system.sendAlert(informativeAlert1)
        system.sendAlert(informativeAlert2)
        system.sendAlert(urgentAlert1)
        system.sendAlert(informativeAlert3)
        system.sendAlert(urgentAlert2)
        system.sendAlert(informativeAlert4)

        val notificationsNonExpired: List<Alert> =
            system
                .getNotificationsFromTopic(topic)
                .map { notification -> notification.alert }

        // U2,U1,I1,I2,I3,I4
        assertEquals(urgentAlert2, notificationsNonExpired[0])
        assertEquals(urgentAlert1, notificationsNonExpired[1])
        assertEquals(informativeAlert1, notificationsNonExpired[1])
        assertEquals(informativeAlert2, notificationsNonExpired[1])
        assertEquals(informativeAlert3, notificationsNonExpired[1])
        assertEquals(informativeAlert4, notificationsNonExpired[1])
    }
}