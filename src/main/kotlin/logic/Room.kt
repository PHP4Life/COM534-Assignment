////////////////////////////// Room.kt /////////////////////////////////////
///////////////////////// Author: Edward Kirr /////////////////////////////
//// Originally made by Charles Clark & William Bladon-Whittam for AE1 ///
/////////// Description: Used to store computer objects and /////////////
//////// subsequently computer booking objects,  ///////////////////////
// this acts as the facade class for the building class and room class /
//////////// In this program, it's also a singleton, //////////////////
////////// as only one instance of the class is made /////////////////
/////////////////// through the whole application ///////////////////
package logic



abstract class Room(
    var roomNumber: Int, val building: Building,
    var timeSlots: List<String> = listOf("9am-11am", "11am-1pm", "1pm-3pm", "3pm-5pm"),
    val daysOfTheWeek: List<String> = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday"),
    val numOfComputer: Int,
) {
    private var computers = mutableListOf<Computer>()

    // All computers in a given room have the same operating system  (Windows, Linux or Mac).
    // Room acts as the abstract class - then use specific OS Room subclasses.

    abstract fun getOperatingSystem(): String

    fun addComputer(computer: Computer) {
        // Add a computer object to the room's mutable list
        computers.add(computer)
    }

    fun addComputers(numOfComputersToAdd: Int) {
        // Parameter:
        // Int - This is number of computer objects the function will create. This get added to the room's mutable list then
        repeat(numOfComputersToAdd) {
            computers.add(Computer(it + 1, this, this.timeSlots, this.daysOfTheWeek));
        }
    }

        fun getComputers() : List<Computer> {
        // Returns the list of computers added to the object
        return computers
    }

    fun findComputerByGlobalId(searchId: String) : Computer? {
        // Looks for a computer with that specific ID, and returns the object
        // if not, null is returned
        for(currentComputer in computers) {
            if(currentComputer.globalId == searchId) {
                return currentComputer
            }
        }
        return null
    }

    fun getBookingsByDay(day: String): MutableList<ComputerBooking> {
        // Parameters:
        // String - The string passed will be a day, based on this computer bookings will be returned that were booked for that day
        // Returns: The filtered list of computer bookings, if any, otherwise an empty list
        val bookings = mutableListOf<ComputerBooking>()
        for (computer in computers) {
            for (booking in computer.getBookings()) {
                if (booking.day == day) {
                    bookings.add(booking)
                }
            }
        }
        return bookings
    }


    override fun toString(): String {
        return "Room $roomNumber in building ${building.name} OS: ${getOperatingSystem()} Computers: $computers"
    }
}

class WindowsRoom(roomNumber: Int, building: Building, timeSlots: List<String>, daysOfTheWeek: List<String>, numOfComputer: Int)
    : Room(roomNumber, building, timeSlots, daysOfTheWeek, numOfComputer) {
    /**
     * Room with Windows Operating systems on the Computers
     */
    override fun getOperatingSystem(): String {
        return "Windows"
    }
}

class MacRoom(roomNumber: Int, building: Building, timeSlots: List<String>, daysOfTheWeek: List<String>, numOfComputer: Int)
    : Room(roomNumber, building, timeSlots, daysOfTheWeek, numOfComputer) {
    /**
     * Room with MAC Operating systems on the Computers
     */
    override fun getOperatingSystem(): String {
        return "Mac"
    }
}

class LinuxRoom(roomNumber: Int, building: Building, timeSlots: List<String>, daysOfTheWeek: List<String>, numOfComputer: Int)
    : Room(roomNumber, building, timeSlots, daysOfTheWeek, numOfComputer) {
    /**
     * Room with Linux Operating systems on the Computers
     */
    override fun getOperatingSystem(): String {
        return "Linux"
    }
}

