package squad.abudhabi.logic.user

import squad.abudhabi.logic.exceptions.EmptyUsernameException
import squad.abudhabi.logic.model.User
import squad.abudhabi.logic.repository.AuthenticationRepository

class SaveLoggedUserUseCase(
    private val repository: AuthenticationRepository,
) {
    operator fun invoke(user: User) {
        if(user.username.isBlank() || user.password.isBlank())  throw EmptyUsernameException()
        repository.saveLoggedUser(user)
    }

}