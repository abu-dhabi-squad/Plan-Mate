package squad.abudhabi.data.authentication.datasource

import squad.abudhabi.logic.model.User

interface AuthenticationDataSource {
    fun getAllUsers(): List<User>
    fun saveUsers(users: List<User>)
}