import daos.RoomDao
import logic.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TestRoomDao() : RoomDao {
    private val rooms: MutableList<Room> = mutableListOf()

    fun addRoom(room: Room) { // Method to add rooms
        rooms.add(room)
    }

    override fun insertRoom(room: Room, buildingName: String): Boolean = true
    override fun getRoomsFromDB(buildingName: String, university: University): List<Room> =  rooms
    override fun updateRoomType(roomNumber: Int, updatedOSType: String): Boolean = true
}

class BuildingTests {

    private lateinit var university: University
    private lateinit var building: Building
    private lateinit var testRoomDao: TestRoomDao

    @BeforeEach
    fun setupBuilding() {
        university = University("Uni")
        testRoomDao = TestRoomDao()
        building = Building("UniBuilding", "UB", university, testRoomDao)
    }


    @Test
    fun testGetRooms() {
        val windowsRoom = WindowsRoom(100, building, listOf("9am-11am"), listOf("Monday"), 10)
        val linuxRoom = LinuxRoom(102, building, listOf("9am-11am"), listOf("Monday"), 10)

        testRoomDao.addRoom(windowsRoom)
        testRoomDao.addRoom(linuxRoom)

        val retrievedRooms = building.getRooms()

        val expectedRooms = listOf(windowsRoom, linuxRoom)

        assertEquals(expectedRooms.size, retrievedRooms.size)
        assertEquals(expectedRooms.map { it.roomNumber }, retrievedRooms.map { it.roomNumber })
        assertEquals(expectedRooms.map { it.getOperatingSystem() }, retrievedRooms.map { it.getOperatingSystem() })
    }

    @Test
    fun testCreateWindowsRoom() {
        val windowsRoom = building.createWindowsRoom(100, listOf("9am-11am"), listOf("Monday"), 10)
        testRoomDao.addRoom(windowsRoom)
        val retrievedRooms = building.getRooms()

        assertEquals(windowsRoom.roomNumber, retrievedRooms[0].roomNumber)
        assertEquals(windowsRoom.building, retrievedRooms[0].building)
        assertEquals("Windows", retrievedRooms[0].getOperatingSystem())
    }

    @Test
    fun testCreateMacRoom() {
        val macRoom = building.createMacRoom(100, listOf("9am-11am"), listOf("Monday"), 10)
        testRoomDao.addRoom(macRoom)
        val retrievedRooms = building.getRooms()

        assertEquals(macRoom.roomNumber, retrievedRooms[0].roomNumber)
        assertEquals(macRoom.building, retrievedRooms[0].building)
        assertEquals("Mac", retrievedRooms[0].getOperatingSystem())
    }


    @Test
    fun testCreateLinuxRoom() {
        val linuxRoom = building.createLinuxRoom(100, listOf("9am-11am"), listOf("Monday"), 10)
        testRoomDao.addRoom(linuxRoom)
        val retrievedRooms = building.getRooms()

        assertEquals(linuxRoom.roomNumber, retrievedRooms[0].roomNumber)
        assertEquals(linuxRoom.building, retrievedRooms[0].building)
        assertEquals("Linux", retrievedRooms[0].getOperatingSystem())
    }

    @Test
    fun testAddRoomToDB() {
        val macRoom = building.createMacRoom(100, listOf("9am-11am"), listOf("Monday"), 10)
        val added = building.addRoomToDB(macRoom)

        assertTrue(added)
    }

    @Test
    fun testUpdateRoomType() {
        val linuxRoom = building.createLinuxRoom(102, listOf("9am-11am"), listOf("Monday"), 10)
        building.updateRoomType(linuxRoom, "Windows")
        val updatedRoom = building.findRoomByNumber(102)
        assertEquals("Windows", updatedRoom?.getOperatingSystem())
    }

    @Test
    fun testUpdateRoomTypeFail() {
        val linuxRoom = building.createLinuxRoom(102, listOf("9am-11am"), listOf("Monday"), 10)
        val updatedOS =building.updateRoomType(linuxRoom, "OpenBSD")
        val updatedRoom = building.findRoomByNumber(102)
        assertFalse(updatedOS)
        assertEquals("Linux", updatedRoom?.getOperatingSystem())
    }


    @Test
    fun testFindRoomByNumber() {
        val macRoom = building.createMacRoom(100, listOf("9am-11am"), listOf("Monday"), 10)
        val linuxRoom = building.createLinuxRoom(102, listOf("9am-11am"), listOf("Monday"), 10)

        val foundRoom = building.findRoomByNumber(100)
        assertEquals(macRoom.roomNumber, foundRoom?.roomNumber)
        assertNotEquals(linuxRoom.roomNumber, foundRoom?.roomNumber)

    }

    @Test
    fun testFindRoomByNumberFail() {
        val randomRoomNumber = building.findRoomByNumber(88)
        assertEquals(null, randomRoomNumber)
    }

}