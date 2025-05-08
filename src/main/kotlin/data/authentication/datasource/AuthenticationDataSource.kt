package data.authentication.datasource

import logic.model.User

interface AuthenticationDataSource {
    fun getUserByUserName(userName: String): User?
    fun getAllUsers(): List<User>
    fun createUser(user: User)
}