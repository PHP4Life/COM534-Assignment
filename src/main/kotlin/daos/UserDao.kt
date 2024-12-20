package daos
import daos.Users.password
import logic.AdminUser
import logic.RegularUser
import logic.User
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

//
object Users : Table("users") {
    val id = integer("id").autoIncrement()
    val name = text("name")
    val password = text("password")
    val email = text("email")
    val userType = text("userType")

}

interface UserDao {
    fun insertUser(user: User) : Boolean
    fun getUsersFromDB() : List<User>
}

class ExposedUserDao : UserDao {

    override fun insertUser(user: User): Boolean {
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