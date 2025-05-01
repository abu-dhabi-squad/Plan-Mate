package squad.abudhabi.logic.repository

import squad.abudhabi.logic.model.User

interface AuthenticationRepository {
    fun getUserByName(user: String): User?
    fun addNewUser(user: User)
}