import daos.BookingDao
import logic.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TestBookingDao() : BookingDao {
    private val bookings: MutableList<ComputerBooking> = mutableListOf()

    fun addBooking(booking: ComputerBooking) { // Method to add rooms
        bookings.add(booking)
    }

    override fun insertBooking(booking: ComputerBooking): Boolean = true
    override fun getBookingsFromDB(computerId: String) : List<ComputerBooking> =  bookings
    override fun getBookingsByUserFromDB(user: String): List<ComputerBooking> = bookings

    override fun userDeleteBookingFromDB(booking: ComputerBooking): Boolean = true
}

class RoomTest {
    private lateinit var university: University
    private lateinit var building: Building
    private lateinit var  room: Room

    @BeforeEach
    fun setupRoom() {
        university = University("Uni")
        building = Building("UniBuilding", "UB", university)
        room = WindowsRoom(102, building, listOf("9am-10am"), listOf("Tuesday"), 10)
    }

    @Test
    fun testAddComputers() {
        room.addComputers(room.numOfComputer)
        assertEquals(room.numOfComputer, room.getComputers().size)
    }

    @Test
    fun testAddComputersNegativeNum() {
        room.addComputers(-2)
        assertEquals(0, room.getComputers().size)
    }

    @Test
    fun testGetComputers() {
        room.addComputers(1)
        val computer = room.getComputers()[0]

        assertEquals("UB102-1", computer.toString())
    }

    @Test
    fun testFindComputerByGlobalId() {
        room.addComputers(2)
        val foundComputer = room.findComputerByGlobalId("UB102-2")

        assertEquals(102, foundComputer?.computerRoom?.roomNumber )
    }

    @Test
    fun testFindComputerByGlobalIdFail() {
        room.addComputers(2)
        val foundComputer = room.findComputerByGlobalId("UB102-3")

        assertEquals(null, foundComputer?.computerRoom?.roomNumber )
    }

    @Test
    fun testGetBookingsByDay() {
        val bookingDao = TestBookingDao()
        val computer = Computer(1, room, listOf("9am-10am"), listOf("Tuesday"),  bookingDao)
        val booking = ComputerBooking("UB102-1", "Tuesday", "9am-10am", "User")
        room.addComputer(computer)
        computer.addBooking(booking)
        bookingDao.addBooking(booking)
        computer.getBookings()
        val retrievedBooking = room.getBookingsByDay("Tuesday")[0]
        assertEquals("UB102-1", retrievedBooking.computerId)
        assertEquals("User", retrievedBooking.student)
        assertEquals("Tuesday", retrievedBooking.day)
        assertEquals("9am-10am", retrievedBooking.timeSlot)
    }

    @Test
    fun testGetBookingsByDayFail() {
        val bookingDao = TestBookingDao()
        val computer = Computer(1, room, listOf("9am-10am"), listOf("Tuesday"),  bookingDao)
        val booking = ComputerBooking("UB102-1", "Tuesday", "9am-10am", "User")
        room.addComputer(computer)
        computer.addBooking(booking)
        bookingDao.addBooking(booking)
        computer.getBookings()
        val retrievedBooking = room.getBookingsByDay("Monday")
        assertEquals(0, retrievedBooking.size)
    }

}