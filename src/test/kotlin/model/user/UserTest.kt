package model.user
import exceptions.NonExistentNotificationException
import model.AlertHandler
import model.alert.Alert
import model.alert.AlertPriority
import model.topic.Topic
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.time.Duration
import java.time.LocalDateTime

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserTest {

    lateinit var system: AlertHandler
    lateinit var user: User
    lateinit var topic: Topic
    lateinit var alert: Alert

    @BeforeEach
    fun setup(){
        system = AlertHandler()
        user = User("UsuarioA")
        topic = Topic("TemaA")
        alert = Alert(1, topic, AlertPriority.INFORMATIVA)
        system.register(user)
        system.registerTopic(topic)
    }

    @Test
    fun `un usuario recibe alertas si esta registrado en el sistema`(){
        system.sendAlert(alert)

        assertTrue(user.hasNotification(alert))
    }

    @Test
    fun `un usuario no recibe alertas si no esta suscripto a ese topico`(){
        system.unsubscribeUserTo(user, topic)

        system.sendAlert(alert)

        assertFalse(user.hasNotification(alert))
    }

    @Test
    fun `un usuario recibe alertas si esta suscripto a ese topico`(){
        val newUser = User("UserNew")
        system.register(newUser)
        system.subscribeUserTo(newUser, topic)

        system.sendAlert(alert)

        assertTrue(user.hasNotification(alert))
    }

    @Test
    fun `si hay varios usuarios solo los suscriptos al tema reciben esa alerta`(){
        val userA = User("UserA")
        val userB = User("UserB")
        val userNotSubscried = User("UserC")
        system.register(userA)
        system.register(userB)
        system.register(userNotSubscried)
        system.subscribeUserTo(userA, topic)
        system.subscribeUserTo(userB, topic)

        system.sendAlert(alert)

        assertTrue(userA.hasNotification(alert))
        assertTrue(userB.hasNotification(alert))
        assertFalse(userNotSubscried.hasNotification(alert))
    }

    @Test
    fun `si hay varios usuarios suscriptos se puede mandar una alerta a uno solo`(){
        val userA = User("UserA")
        val userB = User("UserB")
        val userC = User("UserC")
        system.register(userA)
        system.register(userB)
        system.register(userC)
        system.subscribeUserTo(userA, topic)
        system.subscribeUserTo(userB, topic)
        system.subscribeUserTo(userC, topic)

        system.sendAlertTo(alert, userA)

        assertTrue(userA.hasNotification(alert))
        assertFalse(userB.hasNotification(alert))
        assertFalse(userC.hasNotification(alert))
    }

    @Test
    fun `un usuario por defecto tiene las alertas como no leidas`(){
        user.addNotification(alert)

        assertFalse(user.isReaden(alert))
    }

    @Test
    fun `si un usuario marca como leida una alerta, esta pasa a estar marcada como leida`(){
        user.addNotification(alert)
        user.markAsRead(alert)

        assertTrue(user.isReaden(alert))
    }

    @Test
    fun `si un usuario marca como leida una alerta que no tiene, falla`(){
        val alertB = Alert(2, topic, AlertPriority.INFORMATIVA)

        assertThrows(NonExistentNotificationException::class.java) { user.markAsRead(alertB) }
    }

    @Test
    fun `se puede obtener las notificaciones no expiradas de un usuario`(){
        val oldDate = LocalDateTime.now().minus(Duration.ofDays(10))
        val futureDate = LocalDateTime.now().plus(Duration.ofDays(10))
        val alertNonExpired = Alert(2, topic, AlertPriority.INFORMATIVA, futureDate)
        val alertNonExpired2 = Alert(3, topic, AlertPriority.INFORMATIVA, futureDate)
        val alertExpired = Alert(4, topic, AlertPriority.INFORMATIVA, oldDate)
        user.addNotification(alertNonExpired)
        user.addNotification(alertNonExpired2)
        user.addNotification(alertExpired)

        val alertsNonExpired: List<Alert> = user.getNotifications()

        assertTrue(alertsNonExpired.contains(alertNonExpired))
        assertTrue(alertsNonExpired.contains(alertNonExpired2))
        assertFalse(alertsNonExpired.contains(alertExpired))
    }

    @Test
    fun `las notificaciones marcadas como leidas no se obtienen`(){
        val oldDate = LocalDateTime.now().minus(Duration.ofDays(10))
        val futureDate = LocalDateTime.now().plus(Duration.ofDays(10))
        val alertNonExpired = Alert(2, topic, AlertPriority.INFORMATIVA, futureDate)
        val readAlert = Alert(3, topic, AlertPriority.INFORMATIVA, futureDate)
        val alertExpired = Alert(4, topic, AlertPriority.INFORMATIVA, oldDate)
        user.addNotification(alertNonExpired)
        user.addNotification(readAlert)
        user.addNotification(alertExpired)

        user.markAsRead(readAlert)
        val alertsNonExpired: List<Alert> = user.getNotifications()

        assertTrue(alertsNonExpired.contains(alertNonExpired))
        assertFalse(alertsNonExpired.contains(readAlert))
        assertFalse(alertsNonExpired.contains(alertExpired))
    }

    @Test
    fun `el orden de las notificaciones es LIFO para prioritas y FIFO informativas`(){
        // I1,I2,U1,I3,U2,I4
        val informativeAlert1 = Alert(1, topic, AlertPriority.INFORMATIVA)
        val informativeAlert2 = Alert(2, topic, AlertPriority.INFORMATIVA)
        val informativeAlert3 = Alert(3, topic, AlertPriority.INFORMATIVA)
        val informativeAlert4 = Alert(4, topic, AlertPriority.INFORMATIVA)
        val urgentAlert1 = Alert(1, topic, AlertPriority.URGENTE)
        val urgentAlert2 = Alert(2, topic, AlertPriority.URGENTE)
        user.addNotification(informativeAlert1)
        user.addNotification(informativeAlert2)
        user.addNotification(urgentAlert1)
        user.addNotification(informativeAlert3)
        user.addNotification(urgentAlert2)
        user.addNotification(informativeAlert4)

        val alerts: List<Alert> = user.getNotifications()

        // U2,U1,I1,I2,I3,I4
        assertEquals(urgentAlert2, alerts[0])
        assertEquals(urgentAlert1, alerts[1])
        assertEquals(informativeAlert1, alerts[2])
        assertEquals(informativeAlert2, alerts[3])
        assertEquals(informativeAlert3, alerts[4])
        assertEquals(informativeAlert4, alerts[5])
    }
}