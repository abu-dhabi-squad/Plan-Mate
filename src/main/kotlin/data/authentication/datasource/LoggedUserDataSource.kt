package squad.abudhabi.data.authentication.datasource

import squad.abudhabi.logic.model.User

interface LoggedUserDataSource {
    fun saveLoggedUser(user: User)
    fun getLoggedUser(): User
}