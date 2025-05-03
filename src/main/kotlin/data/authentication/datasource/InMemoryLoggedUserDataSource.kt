package squad.abudhabi.data.authentication.datasource

import squad.abudhabi.logic.model.User

class InMemoryLoggedUserDataSource : LoggedUserDataSource {

    private var loggedUser: User? = null
    override fun saveLoggedUser(user: User) {
        loggedUser=user
    }

    override fun getLoggedUser(): User? = loggedUser

}