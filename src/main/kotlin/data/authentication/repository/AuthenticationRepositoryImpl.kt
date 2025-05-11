package data.authentication.repository

import data.authentication.mapper.MongoUserMapper
import logic.model.User
import logic.repository.AuthenticationRepository
import presentation.logic.utils.hashing.HashingService

class AuthenticationRepositoryImpl(
    private val remoteAuthenticationDataSource: RemoteAuthenticationDataSource,
    private val loggedUserDataSource: LoggedUserDataSource,
    private val hashingService : HashingService,
    private val mongoUserMapper : MongoUserMapper
) : AuthenticationRepository {

    override suspend fun loginUser(username: String, password: String): User? {
        return remoteAuthenticationDataSource.getUserByUsername(username)?.let { mongoUserMapper.dtoToUser(it) }
    }

    override suspend fun getUserByName(username: String): User? {
        val userDto = remoteAuthenticationDataSource.getUserByUsername(username)
        return userDto?.let { mongoUserMapper.dtoToUser(it) }
    }

    override suspend fun createUser(user: User) {
        remoteAuthenticationDataSource.createUser(mongoUserMapper.userToDto(user))
    }

    override fun saveLoggedUser(user: User) {
        val userWithHashedPassword = user.copy(password = hashingService.hash(user.password))
        loggedUserDataSource.saveLoggedUser(userWithHashedPassword)
    }

    override fun getLoggedUser(): User? {
        return loggedUserDataSource.getLoggedUser()
    }
}