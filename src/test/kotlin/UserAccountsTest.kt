import daos.Users
import logic.User
import logic.UserAccounts
import daos.UserDao
import logic.AdminUser
import logic.RegularUser

import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test



class TestUserDao() : UserDao {
    private val users: MutableList<User> = mutableListOf()

    fun addUser(user: User) { // Method to add rooms
        users.add(user)
    }
    override fun insertUser(user: User): Boolean = true
    override fun getUsersFromDB(): List<User> = users
}

class UserAccountsTests {

    private lateinit var userAccounts: UserAccounts
    private lateinit var userDao: UserDao

    @Test
    fun testGetUsers() {
        val userDao = TestUserDao()
        userAccounts = UserAccounts(userDao = userDao)
        userDao.addUser(RegularUser("name", "password", "email"))
        userDao.addUser(AdminUser("admin", "password", "email"))
        val users = userAccounts.getUsers()
        assertEquals("admin", users[1].name)
        assertEquals("name", users[0].name)
    }

    @Test
    fun testGetUsersEmpty() {
        val userDao = TestUserDao()
        userAccounts = UserAccounts(userDao = userDao)
       assertEquals(0, userAccounts.getUsers().size)
    }

    @Test
    fun testSignUp() {
        val userDao = TestUserDao()
        userAccounts = UserAccounts(userDao = userDao)
        val insertedUser = userAccounts.signUp("Name", "password", "email")
        assertEquals("Name", insertedUser?.name)
        assertEquals("Regular", insertedUser?.getUserType())

    }

    @Test
    fun testSignUpExistingUsername() {
        val userDao = TestUserDao()
        userAccounts = UserAccounts(userDao = userDao)
        userDao.addUser(RegularUser("name", "password", "email"))
        val insertedUser = userAccounts.signUp("name", "password", "email")
        assertEquals(null, insertedUser)
    }
}