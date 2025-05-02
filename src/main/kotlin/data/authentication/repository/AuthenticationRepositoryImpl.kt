package squad.abudhabi.data.authentication.repository

import squad.abudhabi.logic.model.User
import squad.abudhabi.logic.repository.AuthenticationRepository

class AuthenticationRepositoryImpl :AuthenticationRepository{
    override fun getUserByName(user: String): User {
        TODO("Not yet implemented")
    }

    override fun loginUser(username: String, password: String): User? {
        TODO("Not yet implemented")
    }

    override fun createUser(user: User) {
        TODO("Not yet implemented")
    }

}