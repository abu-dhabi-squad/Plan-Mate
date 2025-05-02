package squad.abudhabi.logic.authentication
import squad.abudhabi.logic.exceptions.UserNotFoundException
import squad.abudhabi.logic.model.User
import squad.abudhabi.logic.repository.AuthenticationRepository
import squad.abudhabi.logic.utils.HashingService

class LoginByUserNameUseCase(
    private val authRepository: AuthenticationRepository,
    private val hashingService: HashingService
) {
    fun getUser(username: String, password: String): User {
        val hashPassword= hashingService.hash(password)
        return authRepository.loginUser(username,hashPassword)
            ?: throw UserNotFoundException(username)
    }
}