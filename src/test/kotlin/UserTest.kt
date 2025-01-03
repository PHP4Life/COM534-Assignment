import logic.AdminUser
import logic.RegularUser

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class UserTest {
    @Test
    fun sanityTest() {
        val a = 1
        val b = 1
        assertEquals(a, b)
    }

    @Test
    fun testGetRegularUserType() {
        val regularUser = RegularUser("name", "password", "email")
        val type = regularUser.getUserType()
        assertEquals(type, "Regular")
    }

    @Test
    fun testAdminUserType() {
        val adminUser = AdminUser("name", "password", "email")
        val type = adminUser.getUserType()
        assertEquals(type, "Admin")
    }

    @Test
    fun testAdminUserTypeFail() {
        val adminUser = AdminUser("name", "password", "email")
        val type = adminUser.getUserType()
        assertNotEquals("Regular", type)
    }

    @Test
    fun testRegularUserTypeFail() {
        val regularUser = RegularUser("name", "password", "email")
        val type = regularUser.getUserType()
        assertNotEquals("Admin", type)
    }

    @Test
    fun testNameOfUser(){
        val adminUser = AdminUser("name", "password", "email")
        assertEquals(adminUser.name, "name")
    }

    @Test
    fun testEmailOfUser(){
        val adminUser = RegularUser("name", "password", "email")
        assertEquals(adminUser.email, "email")
    }


}