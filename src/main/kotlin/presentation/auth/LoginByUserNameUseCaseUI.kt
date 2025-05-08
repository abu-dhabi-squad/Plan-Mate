package presentation.auth

import logic.authentication.LoginByUserNameUseCase
import presentation.UiLauncher
import presentation.admin.ConsoleAdminMenuView
import presentation.io.InputReader
import presentation.io.Printer
import presentation.user.ConsoleUserMenuView
import logic.model.UserType
import logic.user.SaveLoggedUserUseCase

class LoginByUserNameUseCaseUI(
    private val loginUseCase: LoginByUserNameUseCase,
    private val saveLoggedUserUseCase: SaveLoggedUserUseCase,
    private val inputReader: InputReader,
    private val printer: Printer,
    private val consoleMenuViewUser: ConsoleUserMenuView,
    private val consoleMenuViewAdmin: ConsoleAdminMenuView
) : UiLauncher {
    override suspend fun launchUi() {
        printer.displayLn("===== Login =====")

        val username = promptNonEmptyString("Enter username: ")
        val password = promptNonEmptyString("Enter password: ")

        try {
            val user = loginUseCase(username, password)
            printer.displayLn("Login successful! Welcome ${user.username} [${user.userType}]")
            saveLoggedUserUseCase(user)
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