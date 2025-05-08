package logic.user

import logic.exceptions.EmptyUsernameException
import logic.model.User
import logic.repository.AuthenticationRepository

class SaveLoggedUserUseCase(
    private val repository: AuthenticationRepository,
) {
    operator fun invoke(user: User) {
        if(user.username.isBlank() || user.password.isBlank())  throw EmptyUsernameException()
        repository.saveLoggedUser(user)
    }

}