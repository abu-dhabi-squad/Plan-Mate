package squad.abudhabi.logic.authentication

import squad.abudhabi.logic.exceptions.InvalidCredentialsException
import squad.abudhabi.logic.exceptions.UserNotFoundException
import squad.abudhabi.logic.model.User
import squad.abudhabi.logic.repository.AuthenticationRepository
import squad.abudhabi.logic.utils.HashingService

class LoginByUserNameUseCase(
    private val authRepository: AuthenticationRepository,
    private val hashingService: HashingService
) {
    fun login(username: String, password: String): User {

        return authRepository.getUserByName(username )?.let{ existingUser ->
            val hashedInputPassword = hashingService.hash(password)
            if (existingUser.password != hashedInputPassword) {
                throw InvalidCredentialsException()
            }
            existingUser
        } ?: throw UserNotFoundException(username)

   }

}