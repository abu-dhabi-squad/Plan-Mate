package logic.authentication
import logic.exceptions.UserAlreadyExistsException
import logic.model.User
import logic.exceptions.EmptyUsernameException
import logic.validation.PasswordValidator
import logic.repository.AuthenticationRepository

class CreateMateUserUseCase(
    private val authRepository: AuthenticationRepository,
    private val standardPasswordValidator: PasswordValidator
) {
    suspend operator fun invoke(user: User) {
        validateInputs(user.username, user.password)
        checkUserDoesNotExist(user)
        authRepository.createUser(user)
    }
    private fun validateInputs(username: String, password: String) {
        username.takeIf { it.isNotBlank() } ?:
        throw EmptyUsernameException()
        standardPasswordValidator.validatePassword(password)
    }
    private suspend fun checkUserDoesNotExist(user: User) {
        authRepository.getUserByName(user.username)?.let {
            throw UserAlreadyExistsException(user.username)
        }
    }
}