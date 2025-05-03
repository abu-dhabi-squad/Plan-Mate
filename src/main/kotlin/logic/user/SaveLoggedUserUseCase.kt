package squad.abudhabi.logic.user

import logic.validation.PasswordValidator
import squad.abudhabi.logic.exceptions.EmptyUsernameException
import squad.abudhabi.logic.model.User
import squad.abudhabi.logic.repository.AuthenticationRepository

class SaveLoggedUserUseCase(
    private val repository: AuthenticationRepository,
    private val standardPasswordValidator: PasswordValidator
) {
    operator fun invoke(user: User) {
        validateInputs(user.username, user.password)
        repository.saveLoggedUser(user)
    }
    private fun validateInputs(username: String, password: String) {
        username.takeIf { it.isNotBlank() } ?:
        throw EmptyUsernameException()
        standardPasswordValidator.validatePassword(password)
    }
}