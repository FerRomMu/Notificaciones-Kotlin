package model
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AlertSystemTest {

    lateinit var system: AlertSystem
    val user = User("UsuarioA")
    val topic = Topic("TemaA")

    @BeforeEach
    fun setup(){
        system = AlertSystem()

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
    fun `se puede registrar temas  sobre los cuales se enviarán alertas`(){
        val topicB = Topic("TemaB")

        system.registerTopic(topicB)

        assertTrue(system.isTopicAlert(topicB))
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
}