package data.authentication.datasource.inmemory

import data.authentication.repository.LoggedUserDataSource
import logic.model.User

class InMemoryLoggedUser : LoggedUserDataSource {

    private var loggedUser: User? = null
    override fun saveLoggedUser(user: User) {
        loggedUser=user
    }

    override fun getLoggedUser(): User? = loggedUser

}