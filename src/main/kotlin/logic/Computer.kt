////////////////////////// Computer.kt ///////////////////////////////////
///////////////////////// Author: Edward Kirr /////////////////////////////
////////// Originally made by William Bladon-Whittam for AE1 /////////////
/// Allows you create a booking, retrieve, and delete them //////////////
////// The ComputerBooking data class is the blueprint for //////////////
///// all booking data stored and interacted with //////////////////////

package logic

import daos.BookingDao
import daos.ExposedBookingDao

data class ComputerBooking(
    val computerId: String,
    val day: String,
    val timeSlot: String,
    val student: String
    // Changed student to of type string for less coupling
)

class Computer(val computerNumber: Int, val computerRoom: Room,
               var timeSlots: List<String> = listOf("9am-11am", "11am-1pm", "1pm-3pm", "3pm-5pm"),
               private val daysOfTheWeek: List<String> = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday"),
               private val computerBookingsDao: BookingDao = ExposedBookingDao()
) {
    /**
     * Computer object to store the bookings of the Computer
     * Bookings are stored in a mutable set (using a set over a list as there can't be duplicate bookings)
     * The set will store a ComputerBooking data class with the booking information
     * The booking information includes the user that booked, so other users cannot see/edit another users booking.
     *
     */
    // Previous comments made by Will
    var globalId: String = "${computerRoom.building.code}${computerRoom.roomNumber}-$computerNumber"

    private val bookings: MutableSet<ComputerBooking> = mutableSetOf()

    fun addBooking(booking: ComputerBooking): Boolean {
        /**
         * Add a booking to the Computer. Make sure that there is not already a booking for that computer.
         */
        //TODO:  Changed this function for addition checking and simplified short circuit evaluation,
        return bookings.none { it.computerId == booking.computerId && it.day == booking.day && it.timeSlot == booking.timeSlot } && computerBookingsDao.insertBooking(booking)
    }

    fun deleteBooking(booking: ComputerBooking): Boolean {
        /**
         * Delete a Computer booking. Make sure that the booking exists before trying to delete it.
         */

        //TODO: Change to use .any as opposed to .none
        return if (bookings.any {
                it.computerId == booking.computerId &&
                        it.day == booking.day &&
                        it.timeSlot == booking.timeSlot &&
                        it.student == booking.student
            }) {
            computerBookingsDao.userDeleteBookingFromDB(booking)
            bookings.remove(booking)
            true
        } else {
            false
        }
    }

    fun getBookings() : MutableSet<ComputerBooking> {
        // Interacts with the Booking Dao to retrieve bookings from the database,
        // adds them to the computer object and then returns the private set.
        val bookingsFromDB = computerBookingsDao.getBookingsFromDB(globalId)
        for (computerBooking in bookingsFromDB) {
            bookings.add(computerBooking)}
        return bookings
    }

    fun getComputerBookingByDateTime(day: String, timeSlot: String) : List<ComputerBooking?> {
        // Filters the existing bookings by day and time
        return bookings.filter{it.day == day && it.timeSlot == timeSlot && it.computerId == globalId}
    }

//    fun isComputerBooked(day: String, timeSlot: String): Boolean {
//        /**
//         * Checks if a specific time and day is booked for the computer.
//         */
//        return bookings.any { it.day == day && it.timeSlot == timeSlot }
//    }

    private fun isDateTimeBooked(day: String, timeSlot: String, computerId: String): Boolean {
        /**
         * Checks if a particular date and timeslot is already booked for a specific computer
         */
        return bookings.any { it.day == day && it.timeSlot == timeSlot && it.computerId == computerId }
    }

    fun getAvailableBookingDates(): Map<String, List<String>> {
        /**
         * Gets the available booking dates for the following week for the Computer
         */
        // Maps each day of the week to its available time slots
        val availableBookingSlots = mutableMapOf<String, MutableList<String>>()

        for (day in daysOfTheWeek) {
            val availableTimeSlotsForDate = timeSlots.filter { timeSlot -> !isDateTimeBooked(day, timeSlot, globalId) }

            if (availableTimeSlotsForDate.isNotEmpty()) {
                availableBookingSlots[day] = availableTimeSlotsForDate.toMutableList()
            }
        }
        return availableBookingSlots
    }

//    fun updateGlobalId() { // TODO Remove, no longer required.
//        globalId = "${computerRoom.building.code}${computerRoom.roomNumber}-$computerNumber"
//    }

    override fun toString() : String {
        return globalId
    }
}