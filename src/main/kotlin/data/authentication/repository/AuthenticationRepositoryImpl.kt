package squad.abudhabi.data.authentication.repository

import squad.abudhabi.logic.model.User
import squad.abudhabi.logic.repository.AuthenticationRepository

class AuthenticationRepositoryImpl :AuthenticationRepository{
    override fun getUserByName(user: String): User {
        TODO("Not yet implemented")
    }

    override fun addNewUser(user: User) {
        TODO("Not yet implemented")
    }

}