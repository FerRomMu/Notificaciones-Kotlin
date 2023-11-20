package model.notification
import exceptions.*
import model.AlertSystem
import model.alert.Alert
import model.alert.AlertPriority
import model.topic.Receiver
import model.topic.Topic
import model.user.User
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.time.Duration
import java.time.LocalDateTime

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NotificationSorterTest {

    lateinit var topic: Topic
    lateinit var informativeAlert1: Alert
    lateinit var informativeAlert2: Alert
    lateinit var informativeAlert3: Alert
    lateinit var informativeAlert4: Alert
    lateinit var urgentAlert1: Alert
    lateinit var urgentAlert2: Alert

    lateinit var informativeNotification1: UserNotification
    lateinit var informativeNotification2: UserNotification
    lateinit var informativeNotification3: UserNotification
    lateinit var informativeNotification4: UserNotification
    lateinit var urgentNotification1: UserNotification
    lateinit var urgentNotification2: UserNotification
    lateinit var sorter: NotificationSorter<Notification>

    @BeforeEach
    fun setup(){
        topic = Topic("TemaA")

        informativeAlert1 = Alert(1, topic, AlertPriority.INFORMATIVA)
        informativeAlert2 = Alert(2, topic, AlertPriority.INFORMATIVA)
        informativeAlert3 = Alert(3, topic, AlertPriority.INFORMATIVA)
        informativeAlert4 = Alert(4, topic, AlertPriority.INFORMATIVA)
        urgentAlert1 = Alert(1, topic, AlertPriority.URGENTE)
        urgentAlert2 = Alert(2, topic, AlertPriority.URGENTE)

        informativeNotification1 = UserNotification(informativeAlert1)
        informativeNotification2 = UserNotification(informativeAlert2)
        informativeNotification3 = UserNotification(informativeAlert3)
        informativeNotification4 = UserNotification(informativeAlert4)
        urgentNotification1 = UserNotification(urgentAlert1)
        urgentNotification2 = UserNotification(urgentAlert2)

        sorter = NotificationSorter()
    }

    @Test
    fun `un notification sorter sin elementos devuelve una lista vacia`(){
        assertTrue(sorter.get().isEmpty())
    }

    @Test
    fun `el orden de las notificaciones es LIFO para prioritarias`(){
        sorter.add(urgentNotification1)
        sorter.add(urgentNotification2)

        val notifications = sorter.get()

        assertEquals(urgentNotification2, notifications[0])
        assertEquals(urgentNotification1, notifications[1])
    }

    @Test
    fun `el orden de las notificaciones es FIFO para informativas`(){
        sorter.add(informativeNotification1)
        sorter.add(informativeNotification2)

        val notifications = sorter.get()

        assertEquals(informativeNotification1, notifications[0])
        assertEquals(informativeNotification2, notifications[1])
    }

    @Test
    fun `el orden de las notificaciones es primera las urgentes luego las informativas`(){
        // I1,I2,U1,I3,U2,I4
        sorter.add(informativeNotification1)
        sorter.add(informativeNotification2)
        sorter.add(urgentNotification1)
        sorter.add(informativeNotification3)
        sorter.add(urgentNotification2)
        sorter.add(informativeNotification4)

        val notifications = sorter.get()

        assertEquals(urgentNotification2, notifications[0])
        assertEquals(urgentNotification1, notifications[1])
        assertEquals(informativeNotification1, notifications[2])
        assertEquals(informativeNotification2, notifications[3])
        assertEquals(informativeNotification3, notifications[4])
        assertEquals(informativeNotification4, notifications[5])
    }
}