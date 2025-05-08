package data.authentication.datasource

import logic.model.User

interface LoggedUserDataSource {
    fun saveLoggedUser(user: User)
    fun getLoggedUser(): User?
}