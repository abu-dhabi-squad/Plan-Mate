package squad.abudhabi.presentation.login

class LoginPresenter(
    // here we will call the use cases
) {
    private lateinit var loginView: LoginView

    fun login(username: String?, password: String?) {
        if (username.isNullOrBlank() || password.isNullOrBlank()) {
            loginView.onLoginFailure("Username or password cannot be empty")
            return
        }

    }

    fun bindView(view: LoginView) {
        this.loginView = view;
    }

}