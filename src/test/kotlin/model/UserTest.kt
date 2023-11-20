package model
import model.topic.Topic
import model.user.User
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserTest {

    lateinit var system: AlertSystem
    val user = User("UsuarioA")
    val topic = Topic("TemaA")
    val alert = Alert(1, topic)

    @BeforeEach
    fun setup(){
        system = AlertSystem()

        system.register(user)
        system.registerTopic(topic)
    }

    @Test
    fun `un usuario recibe alertas si esta registrado en el sistema`(){
        system.sendAlert(alert)
        assertEquals(alert, user.notifications.first())
    }
}