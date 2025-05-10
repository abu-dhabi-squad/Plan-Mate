package logic.authentication
import logic.exceptions.UserNotFoundException
import logic.model.User
import logic.validation.PasswordValidator
import logic.exceptions.EmptyUsernameException
import logic.repository.AuthenticationRepository

class LoginByUserNameUseCase(
    private val authRepository: AuthenticationRepository,
    private val loginPasswordValidator: PasswordValidator
) {
    suspend operator fun invoke(username: String, password: String): User {
        validateInputs(username, password)
        return authRepository.loginUser(username,password)
            ?: throw UserNotFoundException(username)
    }
    private fun validateInputs(username: String, password: String) {
        username.takeIf { it.isNotBlank() } ?:
        throw EmptyUsernameException()
        loginPasswordValidator.validatePassword(password)
    }
}