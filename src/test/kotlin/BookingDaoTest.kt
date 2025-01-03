import daos.Bookings
import daos.ExposedBookingDao
import logic.Building
import logic.ComputerBooking
import logic.University

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull


class BookingDaoTests {
    private lateinit var bookingDao: ExposedBookingDao
    private lateinit var university: University
    private lateinit var building: Building


    companion object {
        private var database: Database? = null

        @BeforeAll
        @JvmStatic
        fun setupConnection() {
            database = Database.connect("jdbc:sqlite::memory:", "org.sqlite.JDBC")
        }

        @AfterAll
        @JvmStatic
        fun tearDownConnection() {
            try {
                transaction(database!!) {
                    SchemaUtils.drop(Bookings)
                }
            } finally {
                database = null
            }
        }
    }

    @BeforeEach
    fun setUpDao() {
        bookingDao = ExposedBookingDao()
    }

    @Test
    fun testInsertBooking() {
        transaction(database!!) {
            SchemaUtils.create(Bookings)
            val booking = ComputerBooking("100", "Monday", "9am-11am", "User")
            assertTrue(bookingDao.insertBooking(booking))

            val insertedBooking = Bookings.selectAll().firstOrNull()
            assertNotNull(insertedBooking)
            assertEquals("100", insertedBooking?.get(Bookings.computerId))
            assertEquals("Monday", insertedBooking?.get(Bookings.dayOfTheWeek))
            assertEquals("9am-11am", insertedBooking?.get(Bookings.timeSlot))
            assertEquals("User", insertedBooking?.get(Bookings.bookedByUser))

        }
    }
    @Test
    fun testGetBookingsFromDB() {
        transaction(database!!) {
            SchemaUtils.create(Bookings)
            Bookings.insert {
                it[computerId] = "100"
                it[dayOfTheWeek] = "Wednesday"
                it[timeSlot] = "9am-11am"
                it[bookedByUser] = "User"
            }
            val bookings = bookingDao.getBookingsFromDB("100")
            assertEquals(1, bookings.size)
            assertEquals("100", bookings[0].computerId)
            assertEquals("Wednesday", bookings[0].day)
            assertEquals("9am-11am", bookings[0].timeSlot)
            assertEquals("User", bookings[0].student)
        }
    }

    @Test
    fun testGetBookingsFromDBEmptyList() {
        transaction(database!!) {
            SchemaUtils.create(Bookings)
            val bookings = bookingDao.getBookingsFromDB("100")
            assertEquals(0, bookings.size)
        }
    }

    @Test
    fun testGetBookingByUserFromDB() {
        transaction(database!!) {
            SchemaUtils.create(Bookings)
            Bookings.insert {
                it[computerId] = "100"
                it[dayOfTheWeek] = "Wednesday"
                it[timeSlot] = "9am-11am"
                it[bookedByUser] = "User"
            }
            val bookings = bookingDao.getBookingsByUserFromDB("User")
            assertEquals(1, bookings.size)
            assertEquals("100", bookings[0].computerId)
            assertEquals("Wednesday", bookings[0].day)
            assertEquals("9am-11am", bookings[0].timeSlot)
            assertEquals("User", bookings[0].student)
        }
    }

    @Test
    fun testGetBookingsByUserFromDBEmptyList() {
        transaction(database!!) {
            SchemaUtils.create(Bookings)
            val bookings = bookingDao.getBookingsByUserFromDB("User")
            assertEquals(0, bookings.size)
        }
    }

    @Test
    fun testUserDeleteBookingFromDB() {
        transaction(database!!) {
            SchemaUtils.create(Bookings)
            val booking = ComputerBooking("100", "Monday", "9am-11am", "User")
            Bookings.insert {
                it[computerId] = "100"
                it[dayOfTheWeek] = "Monday"
                it[timeSlot] = "9am-11am"
                it[bookedByUser] = "User"
            }
            assertTrue(bookingDao.userDeleteBookingFromDB(booking))
            assertEquals(null, Bookings.selectAll().firstOrNull() )

        }
    }

    @Test
    fun testUserDeleteBookingFromDBWithSameBooking() {
        transaction(database!!) {
            SchemaUtils.create(Bookings)
            val booking = ComputerBooking("100", "Monday", "9am-11am", "User")
            val booking2 = ComputerBooking("100", "Tuesday", "11am-1pm", "Bob")
            Bookings.insert {
                it[computerId] = "100"
                it[dayOfTheWeek] = "Monday"
                it[timeSlot] = "9am-11am"
                it[bookedByUser] = "User"
            }
            Bookings.insert {
                it[computerId] = "100"
                it[dayOfTheWeek] = "Tuesday"
                it[timeSlot] = "11am-1pm"
                it[bookedByUser] = "Bob"
            }
            bookingDao.userDeleteBookingFromDB(booking)
            bookingDao.userDeleteBookingFromDB(booking)

            val insertedBooking2 = Bookings.selectAll().firstOrNull()
            assertEquals("100", insertedBooking2?.get(Bookings.computerId))
            assertEquals("Tuesday",  insertedBooking2?.get(Bookings.dayOfTheWeek))
            assertEquals("11am-1pm", insertedBooking2?.get(Bookings.timeSlot))
            assertEquals("Bob", insertedBooking2?.get(Bookings.bookedByUser))
        }
    }
}