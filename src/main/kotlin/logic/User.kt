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
// LoggedIn bool is now no longer needed as the state is managed by the GUI

package logic

import daos.BookingDao
import daos.ExposedBookingDao

abstract class User(var name: String,  var password: String, var email: String) {
    // Attributes: name, email & password.
    // Abstract class User, provide a foundation for the
    // subclasses (admin & regular) to based off of
    private val bookingsDao: BookingDao = ExposedBookingDao()


    fun getUserBookings() : List<ComputerBooking> {
        //Returns: A list of computer bookings made that contain the user's name
        // Interacts with the Bookings DAO to get bookings, which are filtered by the username
        return  bookingsDao.getBookingsByUserFromDB(name)
    }


    fun deleteUserBooking(booking: ComputerBooking) : Boolean {
        // Parameters: a Computer booking made by the user
        // Returns: true if the booking was successfully deleted
        if (bookingsDao.userDeleteBookingFromDB(booking)) {
            return true
        } else {return false}
    }
    abstract fun getUserType(): String
}

class RegularUser(name: String, password: String, email: String) : User(name, password, email) {
    // Regular Subclass of User.
    // Provides the ability to search for rooms, create bookings, view them and cancel them
    override fun getUserType(): String {
        // Responsible for identifying the user type
        // Return a string of the user type
        return "Regular"
    }
}

class AdminUser(name: String, password: String, email: String) : User(name, password, email) {
    // Admin subclass of User.
    // Provides full functionality of the interface, such as editing rooms,
    // deleting rooms, editing users and deleting users.
    override fun getUserType(): String {
        // Responsible for identifying the user type
        // Return a string of the user type
        return "Admin"
    }
}