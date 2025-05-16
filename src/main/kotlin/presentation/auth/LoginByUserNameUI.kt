package presentation.auth

import logic.authentication.LoginByUserNameUseCase
import logic.model.User.UserType
import logic.user.SaveLoggedUserUseCase
import presentation.UiLauncher
import presentation.io.Printer
import presentation.user.admin.ConsoleAdminMenuUI
import presentation.user.mate.ConsoleUserMenuUI
import presentation.utils.PromptUtils

class LoginByUserNameUI(
    private val loginUseCase: LoginByUserNameUseCase,
    private val saveLoggedUserUseCase: SaveLoggedUserUseCase,
    private val promptUtils: PromptUtils,
    private val printer: Printer,
    private val consoleMenuViewUser: ConsoleUserMenuUI,
    private val consoleMenuViewAdmin: ConsoleAdminMenuUI
) : UiLauncher {
    override suspend fun launchUi() {
        printer.displayLn("===== Login =====")
        val username = promptUtils.promptNonEmptyString("Enter username: ")
        val password = promptUtils.promptNonEmptyString(("Enter password: "))
        try {
            val user = loginUseCase(username, password)
            printer.displayLn("\nLogin successful! Welcome ${user.username} [${user.userType}]")
            printer.displayLn()
            saveLoggedUserUseCase(user)
            when (user.userType) {
                UserType.ADMIN -> consoleMenuViewAdmin.launchUi()
                UserType.MATE -> consoleMenuViewUser.launchUi()
            }
        } catch (exception: Exception) {
            printer.displayLn("\nLogin failed: ${exception.message}")
        }
    }
}