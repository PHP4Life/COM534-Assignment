/**
 * @author  William Bladon-Whittam
 * @author  Edward Kirr
 */

package logic

import daos.ExposedRoomDao


class Building(val name: String, val code: String, private val university: University) {
    /**
     * Building object to store the rooms of the building
     */
    private var rooms = mutableListOf<Room>()

    fun getRooms() : List<Room> {
        val roomsFromDB = ExposedRoomDao().getRoomsFromDB(name, university)
        return roomsFromDB
    }

    private fun addRoom(room: Room) {
        rooms.add(room)
    }

    fun createWindowsRoom(roomNumber: Int,
                          timeslots: List<String> = listOf("9am-11am", "11am-1pm", "1pm-3pm", "3pm-5pm"),
                          daysOfTheWeek: List<String> = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday"),
                          numOfComputer: Int)
    : WindowsRoom {
        val room = WindowsRoom(roomNumber, this, timeslots, daysOfTheWeek, numOfComputer)
        this.addRoom(room)
        room.addComputers(numOfComputer)
        return room
    }

    fun createMacRoom(roomNumber: Int,
                      timeslots: List<String> = listOf("9am-11am", "11am-1pm", "1pm-3pm", "3pm-5pm"),
                      daysOfTheWeek: List<String> = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday"),
                      numOfComputer: Int)
    : MacRoom {
        val room = MacRoom(roomNumber, this, timeslots, daysOfTheWeek, numOfComputer)
        this.addRoom(room)
        room.addComputers(numOfComputer)
        return room
    }

    fun createLinuxRoom(roomNumber: Int,
                        timeslots: List<String> = listOf("9am-11am", "11am-1pm", "1pm-3pm", "3pm-5pm"),
                        daysOfTheWeek: List<String> = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday"),
                        numOfComputer: Int)
    : LinuxRoom {
        val room = LinuxRoom(roomNumber, this, timeslots, daysOfTheWeek, numOfComputer)
        this.addRoom(room)
        room.addComputers(numOfComputer)
        return room
    }

    fun updateRoomType(room: Room, newType: String) {
        /**
         * To update a room type, the room in the rooms list, is replaced with an updated class for the room
         * type, all values and computers are transferred
         */
        val index = rooms.indexOf(room)
        when (newType) {
            "Windows" -> {
                rooms[index] = WindowsRoom(room.roomNumber, room.building, room.timeSlots, room.daysOfTheWeek, room.numOfComputer)
            }
            "Linux" -> {
                rooms[index] = LinuxRoom(room.roomNumber, room.building, room.timeSlots, room.daysOfTheWeek, room.numOfComputer)
            }
            "Mac" -> {
                rooms[index] = MacRoom(room.roomNumber, room.building, room.timeSlots, room.daysOfTheWeek, room.numOfComputer)
            }
        }
        // Add computers to the new room type
        for (computer in room.getComputers()) {
            rooms[index].addComputer(computer)
        }
    }

    fun deleteRoom(room: Room) {
        rooms.remove(room)
    }

    fun findRoomByOS (searchOS: String) : List<Room> {
        val roomsFound = mutableListOf<Room>()
        for (currentRoom in rooms) {
            if(currentRoom.getOperatingSystem() == searchOS) {
                roomsFound.add(currentRoom)
            }
        }
        return roomsFound
    }

    fun findRoomByNumber (number: Int) : Room? {
        return rooms.find { it.roomNumber == number }
    }

    override fun toString(): String {
        return name
    }
}