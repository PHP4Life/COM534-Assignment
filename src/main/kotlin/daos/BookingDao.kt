package daos

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

import logic.ComputerBooking

object Bookings : Table("bookings") {
    val computerId = text("computerId")
    val dayOfTheWeek = text("dayOfTheWeek")
    val timeSlot = text("timeSlot")
    val bookedByUser = text("bookedByUser")
}

interface BookingDao {
    fun insertBooking(booking: ComputerBooking) : Boolean
    fun getBookingsFromDB(computerId: String) : List<ComputerBooking>
    fun getBookingsByUserFromDB(user: String) : List<ComputerBooking>
    fun userDeleteBookingFromDB(booking: ComputerBooking) : Boolean
}

class ExposedBookingDao : BookingDao {

    override fun insertBooking(booking: ComputerBooking): Boolean {
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