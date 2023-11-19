package model
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AlertSystemTest {

    lateinit var system: AlertSystem
    val user = User("UsuarioA")

    @BeforeEach
    fun setup(){
        system = AlertSystem()
    }

    @Test
    fun `se puede registrar un usuario para recibir alertas`(){
        system.register(user)

        assertTrue(system.isRegitered(user))
    }

}