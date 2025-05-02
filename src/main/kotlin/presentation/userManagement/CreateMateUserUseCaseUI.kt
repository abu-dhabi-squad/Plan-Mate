package squad.abudhabi.presentation.userManagement

import squad.abudhabi.logic.authentication.CreateMateUserUseCase
import squad.abudhabi.logic.exceptions.EmptyUsernameException
import squad.abudhabi.logic.exceptions.UserAlreadyExistsException
import squad.abudhabi.logic.model.User
import squad.abudhabi.logic.model.UserType
import squad.abudhabi.presentation.UiLauncher
import squad.abudhabi.presentation.ui_io.InputReader
import squad.abudhabi.presentation.ui_io.Printer

class CreateMateUserUseCaseUI(
    private val createUserUseCase: CreateMateUserUseCase,
    private val inputReader: InputReader,
    private val printer: Printer
): UiLauncher {

    override fun launchUi() {
        printer.displayLn("===== Create User =====")

        val username = promptNonEmptyString("Enter username: ")
        val password = promptNonEmptyString("Enter password: ")

        val user = User(username = username, password = password, userType = UserType.MATE)

        try {
            createUserUseCase.invoke(user)
            printer.displayLn("User created successfully!")
        } catch (e: EmptyUsernameException) {
            printer.displayLn("${e.message}")
        } catch (e: UserAlreadyExistsException) {
            printer.displayLn("${e.message}")
        }
    }

    private fun promptNonEmptyString(prompt: String): String {
        while (true) {
            printer.display(prompt)
            val input = inputReader.readString()
            if (!input.isNullOrBlank()) return input
            printer.displayLn("Input cannot be empty.")
        }
    }

}