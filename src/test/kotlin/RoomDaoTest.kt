import daos.ExposedRoomDao
import daos.Rooms
import logic.Building
import logic.University
import logic.WindowsRoom

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RoomDaoTests {
    private lateinit var roomDao: ExposedRoomDao
    private lateinit var university: University
    private lateinit var building: Building

    companion object {
        private var database: Database? = null

        @BeforeAll
        @JvmStatic
        fun setupConnection() {
            database = Database.connect("jdbc:sqlite::memory:", "org.sqlite.JDBC")
        }

        @AfterAll
        @JvmStatic
        fun tearDownConnection() {
            try {
                transaction(database!!) {
                    SchemaUtils.drop(Rooms)
                }
            } finally {
                database = null
            }
        }
    }

    @BeforeEach
    fun setUpDao() {
        roomDao = ExposedRoomDao()
        university = University("University")
        building = Building("UniBuilding", "UB", university)
    }

    @Test
    fun testInsertRoom() {
        transaction(database!!) {
            SchemaUtils.create(Rooms)
            val room = WindowsRoom(102, building, listOf("9am-11am", "11am-1pm"), listOf("Monday", "Tuesday"), 10)
            assertTrue(roomDao.insertRoom(room, building.name))

            val insertedRoom = Rooms.selectAll().firstOrNull()
            assertNotNull(insertedRoom)
            assertEquals(102, insertedRoom?.get(Rooms.roomNumber))
            assertEquals("Windows", insertedRoom?.get(Rooms.operatingSystem))
        }
    }

    @Test
    fun testGetRoomsFromDB() {
        transaction(database!!) {
            SchemaUtils.create(Rooms)

            university.createBuilding("UniBuilding", "UB")
            Rooms.insert {
                it[roomNumber] = 102
                it[building] = "UniBuilding"
                it[operatingSystem] = "Windows"
                it[timeSlots] = "9am-11am, 11am-1pm"
                it[daysOfWeek] = "Monday, Tuesday"
                it[numOfComputers] = 5
            }
            val rooms = roomDao.getRoomsFromDB("UniBuilding", university)
            assertEquals(1, rooms.size)
            assertEquals(102, rooms[0].roomNumber)
            assertEquals("Windows", rooms[0].getOperatingSystem())
        }
    }

    @Test
    fun testGetRoomsFromDBBuildingNotFound() {
        transaction(database!!) {
            SchemaUtils.create(Rooms)
            assertEquals(0, roomDao.getRoomsFromDB("UniBuilding", university).size)
        }
    }

    @Test
    fun testUpdateRoomType() {
        transaction(database!!) {
            SchemaUtils.create(Rooms)
            university.createBuilding("UniBuilding", "UB")
            Rooms.insert {
                it[roomNumber] = 102
                it[building] = "UniBuilding"
                it[operatingSystem] = "Windows"
                it[timeSlots] = "9am-11am, 11am-1pm"
                it[daysOfWeek] = "Monday, Tuesday"
                it[numOfComputers] = 5
            }
            roomDao.updateRoomType(102, "Linux")
            val updatedRoom = roomDao.getRoomsFromDB("UniBuilding", university)
            assertEquals("Linux", updatedRoom[0].getOperatingSystem())

        }
    }
}