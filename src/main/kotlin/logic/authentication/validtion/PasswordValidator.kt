package presentation.logic.authentication.validtion

interface PasswordValidator {
    fun validatePassword(password: String)
}