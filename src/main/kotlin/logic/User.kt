////////////////////////////// User.kt ////////////////////////////////////
///////////////////////// Author: Edward Kirr ////////////////////////////
///////////// Description: User class to handle creation of /////////////
////////// different types of users and inform the interface ///////////
///////////////////////////////////////////////////////////////////////

/**
 * During integration a loggedIn attribute needed to be added to track which users are logged in.
 * This is to track what user is logged in, as if a user is logged in they should not be able to delete their own
 * account.
 */

package logic

import daos.ExposedBookingDao

abstract class User(var name: String,  var password: String, var email: String, var loggedIn: Boolean = false) {
    // Attributes: name, email & password.
    // Abstract class User, provide a foundation for the
    // subclasses (admin & regular) to based off of

    //TODO: I was unable to successfully implement this in the booking class, as it meant having to initial rooms and computers for each booking
    fun getUserBookings() : List<ComputerBooking> {
        return ExposedBookingDao().getBookingsByUserFromDB(name)
    }

    fun deleteUserBooking(booking: ComputerBooking) : Boolean {
        if (ExposedBookingDao().userDeleteBookingFromDB(booking)) {
            return true
        } else {return false}
    }
    abstract fun getUserType(): String
}

class RegularUser(name: String, password: String, email: String, loggedIn: Boolean = false) : User(name, password, email, loggedIn) {
    // Regular Subclass of User.
    // Provides the ability to search for rooms, create bookings, view them and cancel them
    override fun getUserType(): String {
        // Responsible for identifying the user type
        // Return a string of the user type
        // TODO: Changed this to return this what user type this is
        return "Regular"
    }
}

class AdminUser(name: String, password: String, email: String, loggedIn: Boolean = false) : User(name, password, email, loggedIn) {
    // Admin subclass of User.
    // Provides full functionality of the interface, such as editing rooms,
    // deleting rooms, editing users and deleting users.
    override fun getUserType(): String {
        // Responsible for identifying the user type
        // Return a string of the user type
        // TODO: Changed this to return this what user type this is
        return "Admin"
    }
}