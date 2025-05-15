package logic.authentication

import logic.exceptions.InvalidCredentialsException
import logic.model.User
import logic.repository.AuthenticationRepository
import logic.utils.hashing.HashingService

class LoginByUserNameUseCase(
    private val authRepository: AuthenticationRepository,
    private val hashingPassword: HashingService
) {
    suspend operator fun invoke(username: String, password: String): User {
        validateInputs(username, password)
        val hashedPassword = hashingPassword.hash(password)
        return authRepository.getUserByName(username)
            ?.let { it.takeIf { it.password == hashedPassword } ?: throw InvalidCredentialsException() }
            ?: throw InvalidCredentialsException()
    }

    private fun validateInputs(username: String ,password: String) {
        username.takeIf { it.isNotBlank() } ?: throw InvalidCredentialsException()
        password.takeIf { it.isNotBlank() } ?: throw InvalidCredentialsException()
    }
}