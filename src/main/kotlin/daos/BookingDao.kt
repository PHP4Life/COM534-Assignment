package daos

import logic.ComputerBooking
import logic.Room
import org.jetbrains.exposed.sql.Table

object Bookings : Table("bookings") {
    val computerId = text("computerId")
    val dayOfTheWeek = text("dayOfTheWeek")
    val bookedByUser = text("bookedByUser")
}

interface BookingDao {
    fun insertBooking(room: Room) : Boolean
    fun getBookingsFromDB(computerId: String) : List<ComputerBooking>
}

class ExposedBookingDao : BookingDao {
    override fun insertBooking(room: Room): Boolean {
        TODO("Not yet implemented")
    }

    override fun getBookingsFromDB(computerId: String): List<ComputerBooking> {
        val bookings = mutableListOf<Room>()
    }
}