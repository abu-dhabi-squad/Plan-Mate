package data.authentication.repository

import logic.model.User

interface LocalAuthenticationDataSource {
    fun getUserByUserName(userName: String): User?
    fun getAllUsers(): List<User>
    fun createUser(user: User)
}
