package squad.abudhabi.presentation.auth

import squad.abudhabi.logic.authentication.LoginByUserNameUseCase
import squad.abudhabi.logic.exceptions.UserNotFoundException
import squad.abudhabi.logic.model.UserType
import squad.abudhabi.presentation.UiLauncher
import squad.abudhabi.presentation.admin.ConsoleAdminMenuView
import squad.abudhabi.presentation.ui_io.InputReader
import squad.abudhabi.presentation.ui_io.Printer
import squad.abudhabi.presentation.user.ConsoleUserMenuView

class LoginByUserNameUseCaseUI(
    private val loginUseCase: LoginByUserNameUseCase,
    private val inputReader: InputReader,
    private val printer: Printer,
    private val consoleMenuViewUser: ConsoleUserMenuView,
    private val consoleMenuViewAdmin: ConsoleAdminMenuView
) : UiLauncher {
    override fun launchUi() {
        printer.displayLn("===== Login =====")

        val username = promptNonEmptyString("Enter username: ")
        val password = promptNonEmptyString("Enter password: ")

        try {
            val user = loginUseCase.getUser(username, password)
            printer.displayLn("Login successful! Welcome ${user.username} [${user.userType}]")
            when (user.userType) {
                UserType.ADMIN -> consoleMenuViewAdmin.launchUi()
                UserType.MATE -> consoleMenuViewUser.launchUi()
            }
        } catch (e: Exception) {
            printer.displayLn("Login failed:${e.message}")
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