package squad.abudhabi.data.authentication.repository

import squad.abudhabi.data.authentication.datasource.AuthenticationDataSource
import squad.abudhabi.logic.exceptions.InvalidCredentialsException
import squad.abudhabi.logic.exceptions.UserAlreadyExistsException
import squad.abudhabi.logic.exceptions.UserNotFoundException
import squad.abudhabi.logic.model.User
import squad.abudhabi.logic.repository.AuthenticationRepository

class AuthenticationRepositoryImpl(
    private val authenticationDataSource: AuthenticationDataSource,
) :AuthenticationRepository{

    override fun login(userName: String, password: String): User? {
        return authenticationDataSource.getUserByUserName(userName)
            ?.takeIf { it.password == password }
            ?: throw InvalidCredentialsException()
    }

    override fun getUserByName(userName: String): User? {
        return authenticationDataSource.getUserByUserName(userName)
            ?:throw UserNotFoundException(userName)
    }

    override fun createUser(user: User) {
        authenticationDataSource.getUserByUserName(user.username)
            ?.let { throw UserAlreadyExistsException(user.username) }
        authenticationDataSource.createUser(user)
    }

}