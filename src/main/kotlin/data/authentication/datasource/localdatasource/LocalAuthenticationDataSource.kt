package data.authentication.datasource.localdatasource

import logic.model.User

interface LocalAuthenticationDataSource {
    suspend fun getUserByUserName(userName: String): User?
    suspend fun getAllUsers(): List<User>
    suspend fun createUser(user: User)
}
