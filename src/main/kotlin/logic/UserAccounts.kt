//////////////////////////// UserAccounts.kt //////////////////////////////////
///////////////////////// Author: Edward Kirr ////////////////////////////////
///// Description: User Accounts class to handle storage of different ///////
//// user types (admin, regular) and operations i.e. deleting, editing /////
///////////////////////////////////////////////////////////////////////////

package logic

import daos.ExposedUserDao
import daos.UserDao

class UserAccounts(private val userDao: UserDao = ExposedUserDao()) {
    // User Accounts class, responsible for storing users in a list and all its operations,
    // editing the user's details and delete a room from the list
    private var users = mutableListOf<User>()
    // Use of aggregation to store users in the mutable list

    fun getUsers(): List<User> {
        //Returns: A List of users from the DB
        val usersFromDB = userDao.getUsersFromDB()
        for (userDB in usersFromDB) {users.add(userDB)}
        return usersFromDB
    }

    fun signUp(name: String, password: String, email: String) : User? {
        // Parameter: 3x string - for username, password and email
        // Returns: A the newly created user,
        // Creates a regular user object,
        // based on the data entered. This then calls the user DAO to insert it into the DB
        // Checks to see if a user exists by username, if it does, it returns null
        // Only able to create regular users, admin users were not apart of the requirements
        val foundUsername = getUsers().find { it.name == name }
        if (foundUsername != null) {return null}
        else {
            val newlyCreatedUser = RegularUser(name, password, email)
            // Only used once in this scope, so far
            userDao.insertUser(newlyCreatedUser)
            return newlyCreatedUser
        }
    }

    override fun toString(): String {
        // Overrides the method to print an itemised list of all the users and its account types (i.e. admin, regular
        // Returns: A string list of users
        val userList = StringBuilder()
        for ((index, user) in users.withIndex()) {
            val userType = when (user) {
                is AdminUser -> "Admin"
                else -> "Regular"
            }
            userList.append("${index + 1}. Name: ${user.name} Type: $userType\n")
        }
        return userList.toString()
    }

}