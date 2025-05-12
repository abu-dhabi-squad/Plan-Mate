package presentation.auth

import logic.authentication.LoginByUserNameUseCase
import logic.model.UserType
import logic.user.SaveLoggedUserUseCase
import presentation.UiLauncher
import presentation.io.Printer
import presentation.presentation.user.admin.ConsoleAdminMenuUI
import presentation.presentation.user.mate.ConsoleUserMenuUI
import presentation.presentation.utils.PromptService

class LoginByUserNameUI(
    private val loginUseCase: LoginByUserNameUseCase,
    private val saveLoggedUserUseCase: SaveLoggedUserUseCase,
    private val promptService: PromptService,
    private val printer: Printer,
    private val consoleMenuViewUser: ConsoleUserMenuUI,
    private val consoleMenuViewAdmin: ConsoleAdminMenuUI
) : UiLauncher {
    override suspend fun launchUi() {
        printer.displayLn("===== Login =====")
        val username = promptService.promptNonEmptyString("Enter username: ")
        val password = promptService.promptNonEmptyString(("Enter password: "))
        try {
            val user = loginUseCase(username, password)
            printer.displayLn("\nLogin successful! Welcome ${user.username} [${user.userType}]")
            printer.displayLn()
            saveLoggedUserUseCase(user)
            when (user.userType) {
                UserType.ADMIN -> consoleMenuViewAdmin.launchUi()
                UserType.MATE -> consoleMenuViewUser.launchUi()
            }
        } catch (e: Exception) {
            printer.displayLn("\nLogin failed: ${e.message}")
        }
    }


}