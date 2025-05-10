package presentation.presentation.user.usermanagement

import logic.authentication.CreateMateUserUseCase
import logic.exceptions.EmptyUsernameException
import logic.exceptions.UserAlreadyExistsException
import logic.model.User
import logic.model.UserType
import presentation.UiLauncher
import presentation.io.InputReader
import presentation.io.Printer

class CreateMateUserUseCaseUI(
    private val createUserUseCase: CreateMateUserUseCase,
    private val inputReader: InputReader,
    private val printer: Printer
): UiLauncher {

    override suspend fun launchUi() {
        printer.displayLn("===== Create User =====")

        val username = promptNonEmptyString("Enter username: ")
        val password = promptNonEmptyString("Enter password: ")

        val user = User(username = username, password = password, userType = UserType.MATE)

        try {
            createUserUseCase(user)
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