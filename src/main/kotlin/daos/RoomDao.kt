////////////////////////////// RoomDao.kt //////////////////////////////////
///////////////////////// Author: Edward Kirr ////////////////////////////////
//// Description: data access object to interact with the Rooms table /////

package daos

import logic.*
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

// Initialises the table with its required columns
object Rooms : Table("rooms") {
    val roomNumber = integer("roomNumber")
    val building = text("building")
    val operatingSystem = text("operatingSystem")
    val timeSlots = text("timeSlots")
    val daysOfWeek = text("daysOfWeek")
    val numOfComputers = integer("NumOfComputers")

    override val primaryKey = PrimaryKey(roomNumber)
}

interface RoomDao {
    // Creates the methods to be exposed by object interfacing with this interface
    fun insertRoom(room: Room, buildingName: String) : Boolean
    fun getRoomsFromDB(buildingName: String, university: University) : List<Room>
    fun updateRoomType(roomNumber: Int, updatedOSType: String) : Boolean
}

class ExposedRoomDao : RoomDao {

    override fun insertRoom(room: Room, buildingName: String): Boolean {
        // Parameters:
        // Room - this is the Room object to be inserted into the database
        // String - this the building's name of which the room belonged to
        // Returns: A bool value based on whether it was successful or not
        return try {
            transaction {
                Rooms.insert {
                    it[roomNumber] = room.roomNumber
                    it[building] = buildingName
                    it[operatingSystem] = room.getOperatingSystem()
                    it[timeSlots] = room.timeSlots.joinToString(",")
                    it[daysOfWeek] = room.daysOfTheWeek.joinToString(",")
                    it[numOfComputers] = room.numOfComputer
                }
                true
            }
        } catch (e: Exception) {
            false
        }
    }

    override fun getRoomsFromDB(buildingName: String, university: University): List<Room> {
        // Parameters:
        // String - the building's name
        // University - The university object to check the building exists
        // Returns: a list of rooms that are a part of the building
        val rooms = mutableListOf<Room>()
        transaction {
            Rooms.selectAll().forEach { row ->
                if (buildingName == row[Rooms.building]) {
                    val building = university.findBuildingByName(buildingName)

                    if (building != null) {
                        val timeSlots = row[Rooms.timeSlots].split(",").map { it.trim() }
                        val weekDays = row[Rooms.daysOfWeek].split(",").map { it.trim() }
                        val room = when (row[Rooms.operatingSystem]) {
                            "Windows" -> building.createWindowsRoom(
                                row[Rooms.roomNumber],
                                timeSlots,
                                weekDays,
                                row[Rooms.numOfComputers]

                            )

                            "Mac" -> building.createMacRoom(
                                row[Rooms.roomNumber],
                                timeSlots,
                                weekDays,
                                row[Rooms.numOfComputers]
                            )

                            "Linux" -> building.createLinuxRoom(
                                row[Rooms.roomNumber],
                                timeSlots,
                                weekDays,
                                row[Rooms.numOfComputers]
                            )

                            else -> {
                                throw IllegalArgumentException("Unknown operating system: ${row[Rooms.operatingSystem]}")
                            }
                        }
                        rooms.add(room)

                    } else {
                        println("Building not found: $buildingName")
                    }
                }
            }
        }
        return rooms
    }

    override fun updateRoomType(roomNumber: Int, updatedOSType: String) : Boolean {
        // Parameters:
        // Int - the room number, to identify the room
        // String - The new OS type that the room will become, this is either Windows, Linux or MacOS
        // Returns: a boolean, if the inserted was successful, it's true
        return try {
            transaction {
                Rooms.update({ Rooms.roomNumber eq roomNumber }) {
                    it[operatingSystem] = updatedOSType
                }
            }
            true
        } catch (e: Exception) { false }
    }
}