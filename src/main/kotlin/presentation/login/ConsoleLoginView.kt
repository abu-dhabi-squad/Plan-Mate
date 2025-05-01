package squad.abudhabi.presentation.login

import squad.abudhabi.logic.model.User
import squad.abudhabi.logic.model.UserType
import squad.abudhabi.presentation.UiLauncher
import squad.abudhabi.presentation.ui_io.InputReader
import squad.abudhabi.presentation.ui_io.Printer

class ConsoleLoginView(
    private val loginPresenter: LoginPresenter,
    private val printer: Printer,
    private val inputReader: InputReader
) : LoginView, UiLauncher {

    init {
        loginPresenter.bindView(this)
        launchUi()
    }

    override fun launchUi() {
        printer.displayLn("Welcome to PlanMate Please Login")
        printer.display("Username: ")
        val username = inputReader.readString()
        printer.display("Password: ")
        val password = inputReader.readString()
        loginPresenter.login(username, password)
    }

    override fun onLoginSuccess(user: User) {
        when(user.userType){
            UserType.ADMIN ->  {/* here we will navigate to admin menu */}
            UserType.MATE -> {/* here we will navigate to Mate menu */}
        }
    }

    override fun onLoginFailure(errorMessage: String) {
        printer.displayLn(errorMessage)
    }

}