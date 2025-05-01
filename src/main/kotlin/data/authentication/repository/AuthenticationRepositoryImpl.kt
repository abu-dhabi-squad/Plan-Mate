package squad.abudhabi.data.authentication.repository

import squad.abudhabi.data.authentication.datasource.AuthenticationDataSource
import squad.abudhabi.logic.model.User
import squad.abudhabi.logic.model.UserType
import squad.abudhabi.logic.repository.AuthenticationRepository

class AuthenticationRepositoryImpl(
    private val authenticationDataSource: AuthenticationDataSource
) :AuthenticationRepository{
    override fun getUserByName(user: String): User {
        return User("", "", "", UserType.MATE)
    }

    override fun addNewUser(user: User) {
        TODO("Not yet implemented")
    }

}