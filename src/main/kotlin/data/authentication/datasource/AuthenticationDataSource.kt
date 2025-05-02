package squad.abudhabi.data.authentication.datasource

import squad.abudhabi.logic.model.User

interface AuthenticationDataSource {
    fun getUserByUserName(userName: String): User?
    fun getAllUsers(): List<User>
    fun createUser(user: User)
}