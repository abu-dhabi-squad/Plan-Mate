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

             User(
            username = username,
            password = hashingService.hash(password),
            userType = userType
        ).apply { checkUserDoesNotExist(this)
            authRepository.addNewUser(this) }



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