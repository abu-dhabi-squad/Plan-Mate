package logic.user

import logic.exceptions.NoLoggedInUserException
import logic.model.User
import logic.repository.AuthenticationRepository

class GetLoggedUserUseCase(
    private val repository: AuthenticationRepository
) {
    operator fun invoke(): User {
        return repository.getLoggedUser()
            ?: throw NoLoggedInUserException()
    }
}