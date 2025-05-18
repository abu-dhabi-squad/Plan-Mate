package presentation.user.usermanagement

import logic.authentication.CreateMateUserUseCase
import logic.exceptions.EmptyUsernameException
import logic.exceptions.UserAlreadyExistsException
import logic.model.User
import logic.model.User.UserType
import presentation.UiLauncher
import presentation.io.Printer
import presentation.utils.PromptUtils
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class CreateMateUserUseCaseUI(
    private val createUserUseCase: CreateMateUserUseCase,
    private val printer: Printer,
    private val promptUtils: PromptUtils
    ) : UiLauncher {
    override suspend fun launchUi() {
        printer.displayLn("===== Create User =====")

        val username = promptUtils.promptNonEmptyString("\nEnter username: ")
        val password = promptUtils.promptNonEmptyString("\nEnter password: ")

        val user = User(username = username, password = password, userType = UserType.MATE)

        try {
            createUserUseCase(user)
            printer.displayLn("\nUser created successfully!")
        } catch (exception: EmptyUsernameException) {
            printer.displayLn("\n${exception.message}")
        } catch (exception: UserAlreadyExistsException) {
            printer.displayLn("\n${exception.message}")
        } catch (exception: Exception) {
            printer.displayLn("\n${exception.message}")
        }
    }
}