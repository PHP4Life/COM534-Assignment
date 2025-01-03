import daos.ExposedUserDao
import daos.Users
import logic.RegularUser

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

class UserDaoTests {
    private lateinit var userDao: ExposedUserDao
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
                    SchemaUtils.drop(Users)
                }
            } finally {
                database = null
            }
        }
    }

    @BeforeEach
    fun setUpDao() {
        userDao = ExposedUserDao()
    }

    @Test
    fun testInsertUser() {
        transaction(database!!) {
            SchemaUtils.create(Users)
            val user = RegularUser("Bob", "Password", "email")
            assertTrue(userDao.insertUser(user))

            val insertedUser = Users.selectAll().firstOrNull()
            assertNotNull(insertedUser)
            assertEquals("Bob", insertedUser?.get(Users.name))
            assertEquals("Password", insertedUser?.get(Users.password))
            assertEquals("email", insertedUser?.get(Users.email))
        }
    }


    @Test
    fun testGetUsersFromDB() {
        transaction(database!!) {
            SchemaUtils.create(Users)
            Users.insert {
                it[name] = "Bob"
                it[password] = "Password"
                it[email] = "email"
                it[userType] = "Regular"
            }
            val users = userDao.getUsersFromDB()
            assertEquals("Bob", users[0].name)
            assertEquals("Regular", users[0].getUserType())
        }

    }

    @Test
    fun testGetUsersFromDBEmptyList() {
        transaction(database!!) {
            SchemaUtils.create(Users)
            val users = userDao.getUsersFromDB()
            assertEquals(0, users.size)
        }
    }
}