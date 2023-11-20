package model.topic

import exceptions.NonExistentSubscriberException
import model.alert.Alert
import model.alert.AlertPriority
import model.user.AlertObserver
import model.user.UserObserver
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.*
import java.time.Duration
import java.time.LocalDateTime

class TopicObservableTest {

    lateinit var topic: Topic
    lateinit var alert: Alert

    @BeforeEach
    fun setup(){
        topic = Topic("TemaA")
        alert = Alert(1, topic, AlertPriority.INFORMATIVA)
    }

    @Test
    fun `Si se agrega un observer este pasa a ser un observer del observable`() {
        val observable = TopicObservable(hashSetOf())
        val observer = mock(AlertObserver::class.java)

        observable.addObserver(observer)

        assertTrue(observable.isObserver(observer))
    }

    @Test
    fun `Si se remueve un observer este deja de ser un observer del observable`() {
        val observer = mock(AlertObserver::class.java)
        val observable = TopicObservable(hashSetOf(observer))

        observable.removeObserver(observer)

        assertFalse(observable.isObserver(observer))
    }

    @Test
    fun `Cuando se notifica a los observers todos reciben un update con la alerta`() {
        val observer1 = mock(AlertObserver::class.java)
        val observer2 = mock(AlertObserver::class.java)
        val observable = TopicObservable(hashSetOf(observer1, observer2))

        observable.notifyObservers(alert)

        verify(observer1).update(alert)
        verify(observer2).update(alert)
    }

    @Test
    fun `Cuando se notifica a un observer solo el recibe un update con la alerta`() {
        val observer1 = mock(AlertObserver::class.java)
        val observer2 = mock(AlertObserver::class.java)
        val observable = TopicObservable(hashSetOf(observer1, observer2))

        observable.notifyObserver(alert, observer1)

        verify(observer1).update(alert)
        verify(observer2, never()).update(alert)
    }

    @Test
    fun `Cuando se notifica a un observer inexistente falla`() {
        val observable = TopicObservable(hashSetOf())
        val observer = mock(AlertObserver::class.java)

        assertThrows(NonExistentSubscriberException::class.java) {
            observable.notifyObserver(alert, observer)
        }
    }

    @Test
    fun `Se pueden obtener las notificaciones enviadas`() {
        val observer = mock(AlertObserver::class.java)
        val observable = TopicObservable(hashSetOf(observer))
        val alert2 = Alert(2, topic, AlertPriority.INFORMATIVA)
        val alert3 = Alert(3, topic, AlertPriority.INFORMATIVA)

        observable.notifyObservers(alert)
        observable.notifyObservers(alert2)
        observable.notifyObservers(alert3)

        assertEquals(3, observable.getNotifications().size)
    }
}