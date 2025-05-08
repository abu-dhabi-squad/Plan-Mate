package data.authentication.repository

import logic.model.User

interface LoggedUserDataSource {
    fun saveLoggedUser(user: User)
    fun getLoggedUser(): User?
}