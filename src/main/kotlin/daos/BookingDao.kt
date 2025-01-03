////////////////////////////// BookingDao.kt //////////////////////////////////
///////////////////////// Author: Edward Kirr ////////////////////////////////
//// Description: data access object to interact with the Booking table /////

package daos

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

import logic.ComputerBooking

// Initialises the table with its required columns
object Bookings : Table("bookings") {
    val computerId = text("computerId")
    val dayOfTheWeek = text("dayOfTheWeek")
    val timeSlot = text("timeSlot")
    val bookedByUser = text("bookedByUser")

    override val primaryKey = PrimaryKey(computerId, dayOfTheWeek, timeSlot)

}

interface BookingDao {
    // Creates the methods to be exposed by object interfacing with this interface
    fun insertBooking(booking: ComputerBooking) : Boolean
    fun getBookingsFromDB(computerId: String) : List<ComputerBooking>
    fun getBookingsByUserFromDB(user: String) : List<ComputerBooking>
    fun userDeleteBookingFromDB(booking: ComputerBooking) : Boolean
}

class ExposedBookingDao : BookingDao {

    override fun insertBooking(booking: ComputerBooking): Boolean {
        // Parameters:
        // ComputerBooking - this is the booking object to be inserted into the database
        // Returns: A bool value based on whether it was successful or not
        return try {
            transaction {
                Bookings.insert {
                    it[computerId] = booking.computerId
                    it[dayOfTheWeek] = booking.day
                    it[timeSlot] = booking.timeSlot
                    it[bookedByUser] = booking.student
                }
                true
            }
        } catch (e: Exception) {
            false
        }
    }

    override fun getBookingsFromDB(computerId: String): List<ComputerBooking> {
        // Parameters:
        // String - this is the id, one of the computer attributes
        // Returns: a list of computer bookings that have the computer's id
        val bookings = mutableListOf<ComputerBooking>()
        transaction {
            Bookings.selectAll().forEach { row ->
                if (computerId == row[Bookings.computerId]) {
                    bookings.add(
                    ComputerBooking(
                        row[Bookings.computerId],
                        row[Bookings.dayOfTheWeek],
                        row[Bookings.timeSlot],
                        row[Bookings.bookedByUser]
                        )
                    )
                }
            }
        }
        return bookings
    }

    override fun getBookingsByUserFromDB(user: String): List<ComputerBooking> {
        // Parameters:
        // String - this is the username of the current user logged in
        // Returns: a list of computer bookings that contain the user's name
        val bookings = mutableListOf<ComputerBooking>()
        transaction {
            Bookings.selectAll().forEach { row ->
                if (user == row[Bookings.bookedByUser]) {
                    bookings.add(
                        ComputerBooking(
                            row[Bookings.computerId],
                            row[Bookings.dayOfTheWeek],
                            row[Bookings.timeSlot],
                            row[Bookings.bookedByUser]
                        )
                    )
                }
            }
        }
        return  bookings
    }

    override fun userDeleteBookingFromDB(booking: ComputerBooking): Boolean {
        // Parameters:
        return try {
            transaction {
                Bookings.deleteWhere {
                    (computerId eq booking.computerId) and (dayOfTheWeek eq booking.day) and (timeSlot eq booking.timeSlot)
                }
                true
            }
        } catch (e: Exception) {
            false
        }
    }

}