////////////////////////////// UserDao.kt //////////////////////////////////
///////////////////////// Author: Edward Kirr ////////////////////////////////
//// Description: data access object to interact with the User table /////

package daos
import daos.Users.password
import logic.AdminUser
import logic.RegularUser
import logic.User
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

// Initialises the table with its required columns
object Users : Table("users") {
    val id = integer("id").autoIncrement()
    val name = text("name")
    val password = text("password")
    val email = text("email")
    val userType = text("userType")

    override val primaryKey = PrimaryKey(id)
}

interface UserDao {
    // Creates the methods to be exposed by object interfacing with this interface
    fun insertUser(user: User) : Boolean
    fun getUsersFromDB() : List<User>
}

    class ExposedUserDao : UserDao {

    override fun insertUser(user: User): Boolean {
        // Parameters:
        // User - this is the user object, that is being created when the user tries to register
        // Returns: A bool value based on whether it was successful or not
        var userId: Int? = null
        transaction {
            userId = Users.insert {
                it[name] = user.name
                it[password] = user.password
                it[email] = user.email
                it[userType] = user.getUserType()
            }[Users.id]
        }
        return userId != null
    }

    override fun getUsersFromDB(): List<User> {
        // Returns: the list of users that are a part the user accounts
        val users = mutableListOf<User>()
        transaction {
            Users.selectAll().forEach {
                users.add(
                    when (it[Users.userType]) {
                        "Regular" -> RegularUser(
                            it[Users.name],
                            it[password],
                            it[Users.email]
                        )
                        "Admin" -> AdminUser(
                            it[Users.name],
                            it[password],
                            it[Users.email],
                        )
                        else -> throw IllegalArgumentException("Unknown user type: ${it[Users.userType]}")
                    }
                )
            }
        }
        return users
    }

}