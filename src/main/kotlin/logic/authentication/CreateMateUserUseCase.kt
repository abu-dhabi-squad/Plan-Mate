package logic.authentication
import squad.abudhabi.logic.exceptions.EmptyUsernameException
import squad.abudhabi.logic.exceptions.UserAlreadyExistsException
import squad.abudhabi.logic.model.User
import squad.abudhabi.logic.repository.AuthenticationRepository
import logic.utils.HashingService
import logic.validation.PasswordValidator

class CreateMateUserUseCase(
    private val authRepository: AuthenticationRepository,
    private val hashingService: HashingService,
    private val standardPasswordValidator: PasswordValidator
) {
    operator fun invoke(user: User) {
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