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

    fun addRoomToDB(room: Room) : Boolean {
        val status = ExposedRoomDao().insertRoom(room, name)
        return status
    }

    fun updateRoomType(room: Room, newOSType: String) : Boolean {
        // TODO: Updated function to update the database and add the newly updated object to the building list. Now returns a boolean to the GUI
        /**
         * To update a room type, the room in the rooms list, is replaced with an updated class for the room
         * type, all values and computers are transferred
         */
        val index = rooms.indexOf(room)
        when (newOSType) {
            "Windows" -> {
                val updatedRoom = WindowsRoom(room.roomNumber, room.building, room.timeSlots, room.daysOfTheWeek, room.numOfComputer)
                rooms[index] = updatedRoom
                return ExposedRoomDao().updateRoomType(room.roomNumber, newOSType)
            }
            "Linux" -> {
                val updatedRoom = LinuxRoom(room.roomNumber, room.building, room.timeSlots, room.daysOfTheWeek, room.numOfComputer)
                rooms[index] = updatedRoom
                return ExposedRoomDao().updateRoomType(room.roomNumber, newOSType)
            }
            "Mac" -> {
                val updatedRoom = MacRoom(room.roomNumber, room.building, room.timeSlots, room.daysOfTheWeek, room.numOfComputer)
                rooms[index] = updatedRoom
                return ExposedRoomDao().updateRoomType(room.roomNumber, newOSType)
            }
        }
        return false
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
        // TODO: Changed this to call the get rooms method, which retrieves from the database
        getRooms()
        return rooms.find { it.roomNumber == number }
    }

    override fun toString(): String {
        return name
    }
}