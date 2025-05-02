package squad.abudhabi.logic.repository

import squad.abudhabi.logic.model.User

interface AuthenticationRepository {
    fun getUserByName(userName: String): User?
    fun createUser(user: User)
}