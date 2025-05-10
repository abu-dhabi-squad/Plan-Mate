package data.authentication.repository

import data.authentication.mapper.MongoUserMapper
import logic.exceptions.InvalidCredentialsException
import logic.model.User
import logic.repository.AuthenticationRepository
import presentation.data.utils.hashing.HashingService

class AuthenticationRepositoryImpl(
    private val remoteAuthenticationDataSource: RemoteAuthenticationDataSource,
    private val loggedUserDataSource: LoggedUserDataSource,
    private val hashingService : HashingService,
    private val mongoUserMapper : MongoUserMapper
) : AuthenticationRepository {

    override suspend fun loginUser(username: String, password: String): User {
        val hashedPassword = hashingService.hash(password)
        return mongoUserMapper.dtoToUser(remoteAuthenticationDataSource.getUserByUserName(username)
            ?.takeIf { it.password == hashedPassword }
            ?: throw InvalidCredentialsException())
    }

    override suspend fun getUserByName(username: String): User? {
        val userDto = remoteAuthenticationDataSource.getUserByUserName(username)
        return userDto?.let { mongoUserMapper.dtoToUser(it) }
    }

    override suspend fun createUser(user: User) {
        val userWithHashedPassword = user.copy(password = hashingService.hash(user.password))
        remoteAuthenticationDataSource.createUser(mongoUserMapper.userToDto(userWithHashedPassword))
    }

    override fun saveLoggedUser(user: User) {
        val userWithHashedPassword = user.copy(password = hashingService.hash(user.password))
        loggedUserDataSource.saveLoggedUser(userWithHashedPassword)
    }

    override fun getLoggedUser(): User? {
        return loggedUserDataSource.getLoggedUser()
    }
}