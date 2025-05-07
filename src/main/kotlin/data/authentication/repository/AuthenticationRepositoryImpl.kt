package data.authentication.repository

import data.authentication.datasource.AuthenticationDataSource
import data.authentication.datasource.localdatasource.LoggedUserDataSource
import logic.exceptions.InvalidCredentialsException
import logic.model.User
import logic.repository.AuthenticationRepository

class AuthenticationRepositoryImpl(
    private val authenticationDataSource: AuthenticationDataSource,
    private val loggedUserDataSource: LoggedUserDataSource
) : AuthenticationRepository {

    override suspend fun loginUser(userName: String, password: String): User {
        return authenticationDataSource.getUserByUserName(userName)
            ?.takeIf { it.password == password }
            ?: throw InvalidCredentialsException()
    }

    override suspend fun getUserByName(userName: String): User? {
        return authenticationDataSource.getUserByUserName(userName)
    }

    override suspend fun createUser(user: User) {
        authenticationDataSource.createUser(user)
    }

    override fun saveLoggedUser(user: User) {
        loggedUserDataSource.saveLoggedUser(user)
    }

    override fun getLoggedUser(): User? {
        return loggedUserDataSource.getLoggedUser()
    }

}