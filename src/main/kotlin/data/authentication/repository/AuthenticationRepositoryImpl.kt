package data.authentication.repository

import data.authentication.datasource.AuthenticationDataSource
import data.authentication.datasource.LoggedUserDataSource
import squad.abudhabi.logic.exceptions.InvalidCredentialsException
import squad.abudhabi.logic.model.User
import logic.repository.AuthenticationRepository

class AuthenticationRepositoryImpl(
    private val authenticationDataSource: AuthenticationDataSource,
    private val loggedUserDataSource: LoggedUserDataSource
) : AuthenticationRepository {

    override fun loginUser(userName: String, password: String): User {
        return authenticationDataSource.getUserByUserName(userName)
            ?.takeIf { it.password == password }
            ?: throw InvalidCredentialsException()
    }

    override fun getUserByName(userName: String): User? {
        return authenticationDataSource.getUserByUserName(userName)
    }

    override fun saveLoggedUser(user: User) {
        loggedUserDataSource.saveLoggedUser(user)
    }

    override fun getLoggedUser(): User? {
        return loggedUserDataSource.getLoggedUser()
    }

    override fun createUser(user: User) {
        authenticationDataSource.createUser(user)
    }

}