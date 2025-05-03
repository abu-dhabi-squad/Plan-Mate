package squad.abudhabi.logic.authentication
import squad.abudhabi.logic.exceptions.EmptyUsernameException
import squad.abudhabi.logic.exceptions.UserNotFoundException
import squad.abudhabi.logic.model.User
import squad.abudhabi.logic.repository.AuthenticationRepository
import squad.abudhabi.logic.utils.HashingService
import squad.abudhabi.logic.validation.PasswordValidator

class LoginByUserNameUseCase(
    private val authRepository: AuthenticationRepository,
    private val hashingService: HashingService,
    private val standardPasswordValidator: PasswordValidator
) {
    operator fun invoke(username: String, password: String): User {
        validateInputs(username, password)
        val hashPassword= hashingService.hash(password)
        return authRepository.loginUser(username,hashPassword)
            ?: throw UserNotFoundException(username)
    }
    private fun validateInputs(username: String, password: String) {
        username.takeIf { it.isNotBlank() } ?:
        throw EmptyUsernameException()
        standardPasswordValidator.validatePassword(password)
    }
}