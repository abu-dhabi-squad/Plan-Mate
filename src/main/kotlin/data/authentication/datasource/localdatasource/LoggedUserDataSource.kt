package data.authentication.datasource.localdatasource

import logic.model.User

interface LoggedUserDataSource {
    fun saveLoggedUser(user: User)
    fun getLoggedUser(): User?
}