package data.authentication.datasource.mongo_datasource

import data.authentication.model.UserDto

interface RemoteAuthenticationDataSource {
    suspend fun getUserByUserName(userName: String): UserDto?
    suspend fun getAllUsers(): List<UserDto>
    suspend fun createUser(userDto: UserDto)
}