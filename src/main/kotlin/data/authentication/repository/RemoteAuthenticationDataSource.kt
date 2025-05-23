package data.authentication.repository

import data.authentication.model.UserDto

interface RemoteAuthenticationDataSource {
    suspend fun getUserByUsername(username: String): UserDto?
    suspend fun getAllUsers(): List<UserDto>
    suspend fun createUser(userDto: UserDto)
}