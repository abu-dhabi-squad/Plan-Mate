package data.authentication.repository

import data.authentication.datasource.mongo_datasource.RemoteAuthenticationDataSource
import data.authentication.datasource.localdatasource.LoggedUserDataSource
import data.authentication.mapper.UserMapper
import logic.exceptions.InvalidCredentialsException
import logic.model.User
import logic.repository.AuthenticationRepository

class AuthenticationRepositoryImpl(
    private val remoteAuthenticationDataSource: RemoteAuthenticationDataSource,
    private val loggedUserDataSource: LoggedUserDataSource,
    private val userMapper : UserMapper
) : AuthenticationRepository {

    override suspend fun loginUser(userName: String, password: String): User {
        return userMapper.dtoToUser(remoteAuthenticationDataSource.getUserByUserName(userName)
            ?.takeIf { it.password == password }
            ?: throw InvalidCredentialsException())
    }

    override suspend fun getUserByName(userName: String): User? {
        val userDto = remoteAuthenticationDataSource.getUserByUserName(userName)
        return userDto?.let { userMapper.dtoToUser(it) }
    }

    override suspend fun createUser(user: User) {
        remoteAuthenticationDataSource.createUser(userMapper.userToDto(user))
    }

    override fun saveLoggedUser(user: User) {
        loggedUserDataSource.saveLoggedUser(user)
    }

    override fun getLoggedUser(): User? {
        return loggedUserDataSource.getLoggedUser()
    }

}