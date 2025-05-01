package squad.abudhabi.presentation.login

import squad.abudhabi.logic.model.User

interface LoginView {
    fun onLoginSuccess(user: User)
    fun onLoginFailure(errorMessage: String)
}