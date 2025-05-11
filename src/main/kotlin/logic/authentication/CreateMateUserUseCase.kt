package logic.authentication
import logic.exceptions.UserAlreadyExistsException
import logic.model.User
import logic.exceptions.EmptyUsernameException
import logic.validation.PasswordValidator
import logic.repository.AuthenticationRepository
import presentation.logic.utils.hashing.HashingService

class CreateMateUserUseCase(
    private val authRepository: AuthenticationRepository,
    private val standardPasswordValidator: PasswordValidator,
    private val hashingService: HashingService
) {
    suspend operator fun invoke(user: User) {
        validateInputs(user.username, user.password)
        checkUserDoesNotExist(user)
        val userWithHashedPassword = user.copy(password = hashingService.hash(user.password))
        authRepository.createUser(userWithHashedPassword)
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