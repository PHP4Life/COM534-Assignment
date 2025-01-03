import daos.BookingDao
import logic.*

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import kotlin.test.assertEquals

class TestComputerBookingDao() : BookingDao {
    private val bookings: MutableList<ComputerBooking> = mutableListOf()

    fun addBooking(booking: ComputerBooking) {
        bookings.add(booking)
    }
    override fun insertBooking(booking: ComputerBooking): Boolean = true
    override fun getBookingsFromDB(computerId: String) : List<ComputerBooking> =  bookings
    override fun getBookingsByUserFromDB(user: String): List<ComputerBooking> = bookings
    override fun userDeleteBookingFromDB(booking: ComputerBooking): Boolean = true
}


class ComputerTests {
    private lateinit var university: University
    private lateinit var building: Building
    private lateinit var room: Room
    private lateinit var bookingDao: BookingDao
    private lateinit var computer: Computer

    @BeforeEach
    fun setUpComputer() {
        university = University("TestUni")
        building = Building("TestBuilding", "TB", university)
        room = WindowsRoom(
            100, building,
            listOf("9am-11am", "11am-1pm", "1pm-3pm", "3pm-5pm"),
            listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday"),
            10
        )
        bookingDao = TestComputerBookingDao()
        computer = Computer(1, room, computerBookingsDao = bookingDao)
    }


    @Test
    fun testAddBooking() {
        val booking = ComputerBooking("TB100-1", "Monday", "9am-11am", "student")
        val addBooking = computer.addBooking(booking)
        assertTrue(addBooking)
    }

    @Test
    fun testAddBookingMultipleBookings() {
        val bookingDao = TestComputerBookingDao()
        val computer =
            Computer(1, room, listOf("9am-11am", "11am-1pm", "1pm-3pm", "3pm-5pm"), listOf("Wednesday"), bookingDao)
        val booking = ComputerBooking("TB100-1", "Wednesday", "9am-11am", "student")
        computer.addBooking(booking)
        bookingDao.addBooking(booking)
        computer.getBookings()
        val booking2 = ComputerBooking("TB100-1", "Wednesday", "11am-1pm", "student")
        val addBooking2 = computer.addBooking(booking2)
        assertTrue(addBooking2)
    }

    @Test
    fun testAddBookingFail() {
        val bookingDao = TestComputerBookingDao()
        val computer =
            Computer(1, room, listOf("9am-11am", "11am-1pm", "1pm-3pm", "3pm-5pm"), listOf("Wednesday"), bookingDao)
        val booking = ComputerBooking("TB100-1", "Wednesday", "9am-11am", "student")
        computer.addBooking(booking)
        bookingDao.addBooking(booking)
        computer.getBookings()
        val addBooking2 = computer.addBooking(booking)
        assertFalse(addBooking2)
    }

    @Test
    fun testDeleteBooking() {
        val bookingDao = TestComputerBookingDao()
        val computer =
            Computer(1, room, listOf("9am-11am", "11am-1pm", "1pm-3pm", "3pm-5pm"), listOf("Wednesday"), bookingDao)
        val booking = ComputerBooking("TB100-1", "Wednesday", "9am-11am", "student")
        computer.addBooking(booking)
        bookingDao.addBooking(booking)
        computer.getBookings()
        assertTrue(computer.deleteBooking(booking))
    }

    @Test
    fun testDeleteBookingEmpty() {
        val bookingDao = TestComputerBookingDao()
        val computer =
            Computer(1, room, listOf("9am-11am", "11am-1pm", "1pm-3pm", "3pm-5pm"), listOf("Wednesday"), bookingDao)
        val booking = ComputerBooking("TB100-1", "Wednesday", "9am-11am", "student")
        computer.deleteBooking(booking)
        assertFalse(computer.deleteBooking(booking))
    }

    @Test
    fun testGetBookings() {
        val bookingDao = TestComputerBookingDao()
        val computer =
            Computer(1, room, listOf("9am-11am", "11am-1pm", "1pm-3pm", "3pm-5pm"), listOf("Wednesday"), bookingDao)
        val booking = ComputerBooking("TB100-1", "Wednesday", "9am-11am", "student")
        val booking2 = ComputerBooking("TB100-1", "Wednesday", "1pm-3pm", "student")
        bookingDao.addBooking(booking)
        bookingDao.addBooking(booking2)
        assertEquals(2, computer.getBookings().size)
    }

    @Test
    fun testGetBookingsEmpty() {
        val bookingDao = TestComputerBookingDao()
        val computer =
            Computer(1, room, listOf("9am-11am", "11am-1pm", "1pm-3pm", "3pm-5pm"), listOf("Wednesday"), bookingDao)
        assertEquals(0, computer.getBookings().size)

    }

    @Test
    fun testGetComputerBookingByDateTime() {
        val bookingDao = TestComputerBookingDao()
        val computer =
            Computer(1, room, listOf("9am-11am", "11am-1pm", "1pm-3pm", "3pm-5pm"), listOf("Wednesday"), bookingDao)
        val booking = ComputerBooking("TB100-1", "Wednesday", "9am-11am", "student")
        val booking2 = ComputerBooking("TB100-1", "Wednesday", "1pm-3pm", "student")
        computer.addBooking(booking)
        computer.addBooking(booking2)
        bookingDao.addBooking(booking)
        bookingDao.addBooking(booking2)
        computer.getBookings()
        val retrievedBooking = computer.getComputerBookingByDateTime("Wednesday", "9am-11am")[0]
        assertEquals("TB100-1", retrievedBooking?.computerId)
        assertEquals("9am-11am", retrievedBooking?.timeSlot)
        assertEquals("Wednesday", retrievedBooking?.day)

    }


    @Test
    fun testGetAvailableBookingDates() {
        val bookingDao = TestComputerBookingDao()
        val computer =
            Computer(1, room, listOf("9am-11am", "11am-1pm", "1pm-3pm", "3pm-5pm"), listOf("Wednesday"), bookingDao)
        val booking = ComputerBooking("TB100-1", "Wednesday", "9am-11am", "student")
        computer.addBooking(booking)
        bookingDao.addBooking(booking)
        computer.getBookings()
        val returnedDates = computer.getAvailableBookingDates()
        val expectedDates = mapOf("Wednesday" to listOf("11am-1pm", "1pm-3pm", "3pm-5pm"))
        assertEquals(expectedDates, returnedDates)
    }
}
