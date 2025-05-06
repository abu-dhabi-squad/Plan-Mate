package data.authentication.datasource

import squad.abudhabi.logic.model.User

interface AuthenticationDataSource {
    suspend fun getUserByUserName(userName: String): User?
    suspend fun getAllUsers(): List<User>
    suspend fun createUser(user: User)
}