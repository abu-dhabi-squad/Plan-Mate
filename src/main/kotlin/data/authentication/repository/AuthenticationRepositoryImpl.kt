package data.authentication.repository

import data.authentication.mapper.MongoUserMapper
import logic.exceptions.InvalidCredentialsException
import logic.model.User
import logic.repository.AuthenticationRepository
import data.authentication.datasource.AuthenticationDataSource
import data.authentication.datasource.LoggedUserDataSource

class AuthenticationRepositoryImpl(
    private val remoteAuthenticationDataSource: RemoteAuthenticationDataSource,
    private val loggedUserDataSource: LoggedUserDataSource,
    private val mongoUserMapper : MongoUserMapper
) : AuthenticationRepository {

    override suspend fun loginUser(userName: String, password: String): User {
        return mongoUserMapper.dtoToUser(remoteAuthenticationDataSource.getUserByUserName(userName)
            ?.takeIf { it.password == password }
            ?: throw InvalidCredentialsException())
    }

    override suspend fun getUserByName(userName: String): User? {
        val userDto = remoteAuthenticationDataSource.getUserByUserName(userName)
        return userDto?.let { mongoUserMapper.dtoToUser(it) }
    }

    override suspend fun createUser(user: User) {
        remoteAuthenticationDataSource.createUser(mongoUserMapper.userToDto(user))
    }

    override fun saveLoggedUser(user: User) {
        loggedUserDataSource.saveLoggedUser(user)
    }

    override fun getLoggedUser(): User? {
        return loggedUserDataSource.getLoggedUser()
    }
}