package data.authentication.repository

import data.authentication.mapper.UserMapper
import logic.model.User
import logic.repository.AuthenticationRepository
import presentation.logic.utils.hashing.HashingService

class AuthenticationRepositoryImpl(
    private val remoteAuthenticationDataSource: RemoteAuthenticationDataSource,
    private val loggedUserDataSource: LoggedUserDataSource,
    private val hashingService: HashingService,
    private val userMapper: UserMapper
) : AuthenticationRepository {

    override suspend fun loginUser(username: String, password: String): User? {
        return remoteAuthenticationDataSource.getUserByUsername(username)?.let { userMapper.dtoToUser(it) }
    }

    override suspend fun getUserByName(username: String): User? {
        val userDto = remoteAuthenticationDataSource.getUserByUsername(username)
        return userDto?.let { userMapper.dtoToUser(it) }
    }

    override suspend fun createUser(user: User) {
        remoteAuthenticationDataSource.createUser(userMapper.userToDto(user))
    }

    override fun saveLoggedUser(user: User) {
        val userWithHashedPassword = user.copy(password = hashingService.hash(user.password))
        loggedUserDataSource.saveLoggedUser(userWithHashedPassword)
    }

    override fun getLoggedUser(): User? {
        return loggedUserDataSource.getLoggedUser()
    }
}