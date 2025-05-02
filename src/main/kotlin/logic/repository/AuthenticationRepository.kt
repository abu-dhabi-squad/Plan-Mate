package squad.abudhabi.logic.repository

import squad.abudhabi.logic.model.User

interface AuthenticationRepository {
    fun login(userName: String, password: String): User?
    fun getUserByName(userName: String): User?
    fun createUser(user: User)
}