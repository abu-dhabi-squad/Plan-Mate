package logic.authentication

import logic.exceptions.UserNotFoundException
import logic.model.User
import logic.validation.PasswordValidator
import logic.exceptions.EmptyUsernameException
import logic.exceptions.InvalidCredentialsException
import logic.repository.AuthenticationRepository
import presentation.logic.utils.hashing.HashingService

class LoginByUserNameUseCase(
    private val authRepository: AuthenticationRepository,
    private val loginPasswordValidator: PasswordValidator,
    private val hashingPassword: HashingService
) {
    suspend operator fun invoke(username: String, password: String): User {
        validateInputs(username, password)
        val hashedPassword = hashingPassword.hash(password)
        return authRepository.loginUser(username, hashedPassword)
            ?.let { it.takeIf { it.password == hashedPassword } ?: throw InvalidCredentialsException() }
            ?: throw UserNotFoundException(username)
    }

    private fun validateInputs(username: String, password: String) {
        username.takeIf { it.isNotBlank() } ?: throw EmptyUsernameException()
        loginPasswordValidator.validatePassword(password)
    }
}