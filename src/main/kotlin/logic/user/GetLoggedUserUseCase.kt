package squad.abudhabi.logic.user

import squad.abudhabi.logic.exceptions.NoLoggedInUserException
import squad.abudhabi.logic.model.User
import logic.repository.AuthenticationRepository

class GetLoggedUserUseCase (
    private val repository: AuthenticationRepository
) {
    operator fun invoke(): User {
        return repository.getLoggedUser()
            ?:throw NoLoggedInUserException()
    }
}