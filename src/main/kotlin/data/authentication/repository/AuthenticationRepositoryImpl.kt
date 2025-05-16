package data.authentication.repository

import data.authentication.mapper.UserMapper
import logic.model.User
import logic.repository.AuthenticationRepository
import data.utils.BaseRepository

class AuthenticationRepositoryImpl(
    private val remoteAuthenticationDataSource: RemoteAuthenticationDataSource,
    private val loggedUserDataSource: LoggedUserDataSource,
    private val userMapper: UserMapper
) : AuthenticationRepository, BaseRepository() {

    override suspend fun getUserByName(username: String): User? {
        val userDto = remoteAuthenticationDataSource.getUserByUsername(username)
        return wrapResponse { userDto?.let { userMapper.dtoToUser(it) } }
    }

    override suspend fun createUser(user: User) = wrapResponse {
        remoteAuthenticationDataSource.createUser(userMapper.userToDto(user))
    }

    override fun saveLoggedUser(user: User) {
        loggedUserDataSource.saveLoggedUser(user)
    }

    override fun getLoggedUser(): User? {
        return loggedUserDataSource.getLoggedUser()
    }
}