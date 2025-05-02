package squad.abudhabi.logic.authentication
import squad.abudhabi.logic.exceptions.EmptyUsernameException
import squad.abudhabi.logic.exceptions.UserAlreadyExistsException
import squad.abudhabi.logic.model.User
import squad.abudhabi.logic.model.UserType
import squad.abudhabi.logic.repository.AuthenticationRepository
import squad.abudhabi.logic.utils.HashingService
import squad.abudhabi.logic.validation.PasswordValidator

class CreateMateUserUseCase(
    private val authRepository: AuthenticationRepository,
    private val hashingService: HashingService,
    private val standardPasswordValidator: PasswordValidator
) {
    fun create(user: User) {
        validateInputs(user.username, user.password)
        checkUserDoesNotExist(user)
        authRepository.createUser(
            user.copy(password = hashingService.hash(user.password))
        )
    }

    private fun validateInputs(username: String, password: String) {
        username.takeIf { it.isNotBlank() } ?:
        throw EmptyUsernameException()
        standardPasswordValidator.validatePassword(password)
    }

    private fun checkUserDoesNotExist(user: User) {
        authRepository.getUserByName(user.username)?.let {
            throw UserAlreadyExistsException(user.username)
        }

    }
}