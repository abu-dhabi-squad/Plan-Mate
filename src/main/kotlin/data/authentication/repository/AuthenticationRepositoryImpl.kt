package squad.abudhabi.data.authentication.repository

import squad.abudhabi.data.authentication.datasource.AuthenticationDataSource
import squad.abudhabi.logic.exceptions.UserAlreadyExistsException
import squad.abudhabi.logic.exceptions.UserNotFoundException
import squad.abudhabi.logic.model.User
import squad.abudhabi.logic.repository.AuthenticationRepository

class AuthenticationRepositoryImpl(
    private val authenticationDataSource: AuthenticationDataSource
) :AuthenticationRepository{
    override fun getUserByName(userName: String): User? {
        return authenticationDataSource.getAllUsers().find { it.username==userName }
            ?:throw UserNotFoundException(userName)
    }

    override fun addNewUser(user: User) {
        authenticationDataSource.getAllUsers()
            .find { it.username == user.username }
            ?.let { throw UserAlreadyExistsException(user.username) }
        authenticationDataSource.saveUsers(authenticationDataSource.getAllUsers() + user)
    }


}