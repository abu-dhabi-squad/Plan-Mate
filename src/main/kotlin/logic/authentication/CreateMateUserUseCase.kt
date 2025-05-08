package logic.authentication
import logic.exceptions.EmptyUsernameException
import logic.exceptions.UserAlreadyExistsException
import logic.model.User
import logic.repository.AuthenticationRepository
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