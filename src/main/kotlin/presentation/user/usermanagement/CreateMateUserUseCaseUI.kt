package presentation.presentation.user.usermanagement

import logic.authentication.CreateMateUserUseCase
import logic.exceptions.EmptyUsernameException
import logic.exceptions.UserAlreadyExistsException
import logic.model.User
import logic.model.UserType
import presentation.UiLauncher
import presentation.io.Printer
import presentation.presentation.utils.PromptService

class CreateMateUserUseCaseUI(
    private val createUserUseCase: CreateMateUserUseCase,
    private val printer: Printer,
    private val promptService: PromptService
    ) : UiLauncher {
    override suspend fun launchUi() {
        printer.displayLn("===== Create User =====")

        val username = promptService.promptNonEmptyString("\nEnter username: ")
        val password = promptService.promptNonEmptyString("\nEnter password: ")

        val user = User(username = username, password = password, userType = UserType.MATE)

        try {
            createUserUseCase(user)
            printer.displayLn("\nUser created successfully!")
        } catch (e: EmptyUsernameException) {
            printer.displayLn("${e.message}")
        } catch (e: UserAlreadyExistsException) {
            printer.displayLn("${e.message}")
        }
    }
}