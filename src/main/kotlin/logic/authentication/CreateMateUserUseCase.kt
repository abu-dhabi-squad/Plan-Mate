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
    fun create(username: String, password: String, userType: UserType) {
        validateInputs(username, password)

        val newUser = User(
            username = username,
            password = hashingService.hash(password),
            userType = userType
        )

        checkUserDoesNotExist(newUser)

        authRepository.addNewUser(newUser)

    }

    private fun validateInputs(username: String, password: String) {
        if (username.isBlank()) {
            throw EmptyUsernameException()
        }

        standardPasswordValidator.validatePassword(password)
    }

    private fun checkUserDoesNotExist(user: User) {
        val existingUser = authRepository.getUserByName(user.username)
        if (existingUser != null) {
            throw UserAlreadyExistsException(user.username)
        }

       }

}