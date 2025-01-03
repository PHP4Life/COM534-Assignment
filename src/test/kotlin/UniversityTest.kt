import logic.Building
import logic.University
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull


class UniversityTests {

    private lateinit var university: University
    private lateinit var building: Building

    @BeforeEach
    fun setupUniversity() {
        university = University("Uni")
        building = university.createBuilding("UniBuilding", "UB")
    }
    @Test
    fun testCreateUniversity() {
        assertTrue(university is University)
        assertEquals("Uni", university.name)
    }

    @Test
    fun testCreateBuilding() {
        assertTrue(building is Building)
        assertEquals("UniBuilding", building.name)
        assertEquals("UB", building.code)
    }

    @Test
    fun testGetBuildings() {
        val buildingInList = university.getBuildings()[0]
        assertEquals("UniBuilding", buildingInList.name)
        assertEquals("UB", buildingInList.code)
    }

    @Test
    fun testFindBuildingByName() {
        val foundBuilding = university.findBuildingByName("UniBuilding")
        assertEquals("UniBuilding", foundBuilding?.name)
        assertEquals("UB", foundBuilding?.code)
    }

    @Test
    fun testFindBuildingByNameFail() {
        assertNull(university.findBuildingByName(" "))
    }

    @Test
    fun testUniversityToString() {
        assertEquals("University: Uni Buildings: [UniBuilding] ", university.toString())
    }

}