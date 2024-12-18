/**
 * @author  William Bladon-Whittam
 * @author  Charlie Clark
 */

package logic

abstract class Room(
    var roomNumber: Int, val building: Building,
    var timeSlots: List<String> = listOf("9am-11am", "11am-1pm", "1pm-3pm", "3pm-5pm"),
    val daysOfTheWeek: List<String> = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday"),
    val numOfComputer: Int
) {
    private var computers = mutableListOf<Computer>()

    abstract fun getOperatingSystem(): String

    fun addComputer(computer: Computer) {
        computers.add(computer)
    }

    fun addComputers(numOfComputersToAdd: Int) {
        repeat(numOfComputersToAdd) {
            computers.add(Computer(it + 1, this, this.timeSlots, this.daysOfTheWeek));
        }
    }

    fun getComputers() : List<Computer> {
        return computers
    }

    fun findComputerByGlobalId(searchId: String) : Computer? {
        for(currentComputer in computers) {
            if(currentComputer.globalId == searchId) {
                return currentComputer
            }
        }
        return null
    }

    fun getBookingsByDay(day: String): MutableList<ComputerBooking> {
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

    fun updateComputersGlobalId() {
        for (computer in computers) {
            computer.updateGlobalId()
        }
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

