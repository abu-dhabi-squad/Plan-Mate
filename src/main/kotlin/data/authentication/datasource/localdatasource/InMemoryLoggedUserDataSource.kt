package data.authentication.datasource.localdatasource

import data.authentication.repository.LoggedUserDataSource
import logic.model.User

class InMemoryLoggedUserDataSource : LoggedUserDataSource {

    private var loggedUser: User? = null
    override fun saveLoggedUser(user: User) {
        loggedUser=user
    }

    override fun getLoggedUser(): User? = loggedUser

}