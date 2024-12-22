package daos

import logic.*
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

object Rooms : Table("rooms") {
    val roomNumber = integer("roomNumber")
    val building = text("building")
    val operatingSystem = text("operatingSystem")
    val timeSlots = text("timeSlots")
    val daysOfWeek = text("daysOfWeek")
    val numOfComputers = integer("NumOfComputers")
}

interface RoomsDao {
    fun insertRoom(room: Room, buildingName: String) : Boolean
    fun getRoomsFromDB(buildingName: String, university: University) : List<Room>
    fun updateRoomType(roomNumber: Int, updatedOSType: String) : Boolean
}

class ExposedRoomDao : RoomsDao {

    override fun insertRoom(room: Room, buildingName: String): Boolean {
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

    override fun updateRoomType(roomNumber: Int, updatedOSType: String) : Boolean {
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