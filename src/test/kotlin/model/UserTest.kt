package model
import exceptions.NonExistentSubscriberException
import exceptions.NonExistentUserException
import model.topic.Topic
import model.user.User
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserTest {

    lateinit var system: AlertSystem
    lateinit var user: User
    lateinit var topic: Topic
    lateinit var alert: Alert

    @BeforeEach
    fun setup(){
        system = AlertSystem()
        user = User("UsuarioA")
        topic = Topic("TemaA")
        alert = Alert(1, topic)
        system.register(user)
        system.registerTopic(topic)
    }

    @Test
    fun `un usuario recibe alertas si esta registrado en el sistema`(){
        system.sendAlert(alert)

        assertEquals(alert, user.notifications.first())
    }

    @Test
    fun `un usuario no recibe alertas si no esta suscripto a ese topico`(){
        system.unsubscribeUserTo(user, topic)

        system.sendAlert(alert)

        assertTrue(user.notifications.isEmpty())
    }

    @Test
    fun `un usuario recibe alertas si esta suscripto a ese topico`(){
        val newUser = User("UserNew")
        system.register(newUser)
        system.subscribeUserTo(newUser, topic)

        system.sendAlert(alert)

        assertEquals(alert, user.notifications.first())
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

        assertEquals(alert, userA.notifications.first())
        assertEquals(alert, userB.notifications.first())
        assertTrue(userNotSubscried.notifications.isEmpty())
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

        assertEquals(alert, userA.notifications.first())
        assertTrue(userB.notifications.isEmpty())
        assertTrue(userC.notifications.isEmpty())
    }
}