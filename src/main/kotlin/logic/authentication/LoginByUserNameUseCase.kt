package logic.authentication
import logic.exceptions.UserNotFoundException
import logic.model.User
import logic.utils.HashingService
import logic.validation.PasswordValidator
import logic.exceptions.EmptyUsernameException
import logic.repository.AuthenticationRepository

class LoginByUserNameUseCase(
    private val authRepository: AuthenticationRepository,
    private val hashingService: HashingService,
    private val loginPasswordValidator: PasswordValidator
) {
    suspend operator fun invoke(username: String, password: String): User {
        validateInputs(username, password)
        val hashPassword= hashingService.hash(password)
        return authRepository.loginUser(username,hashPassword)
            ?: throw UserNotFoundException(username)
    }
    private fun validateInputs(username: String, password: String) {
        username.takeIf { it.isNotBlank() } ?:
        throw EmptyUsernameException()
        loginPasswordValidator.validatePassword(password)
    }
}