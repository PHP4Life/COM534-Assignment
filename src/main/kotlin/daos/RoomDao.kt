package daos

import logic.*
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object Rooms : Table("rooms") {
    val roomNumber = integer("roomNumber")
    val building = text("building")
    val operatingSystem = text("operatingSystem")
    val timeSlots = text("timeSlots")
    val daysOfWeek = text("daysOfWeek")
    val numOfComputers = integer("NumOfComputers")
}

interface RoomsDao {
    fun insertRoom(room: Room) : Boolean
    fun getRoomsFromDB(buildingName: String, university: University) : List<Room>
}

class ExposedRoomDao : RoomsDao {
    override fun insertRoom(room: Room): Boolean {
        TODO("Not yet implemented")
    }

    override fun getRoomsFromDB(buildingName: String, university: University): List<Room> {
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
                                // Handle unknown operating systems
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
}