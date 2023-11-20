package model.user
import model.alert.Alert
import model.alert.AlertPriority
import model.topic.Topic
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class UserObserverTest {

    @Test
    fun `Cuando se notifica, se debe llamar a addNotification en el usuario`() {
        val topic = Topic("TemaA")
        val alert = Alert(1, topic, AlertPriority.INFORMATIVA)
        val user = User("UserA")
        val observer = UserObserver(user)

        observer.update(alert)

        assertTrue(user.hasNotification(alert))
    }
}