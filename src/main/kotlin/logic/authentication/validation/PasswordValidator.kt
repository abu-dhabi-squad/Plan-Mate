package logic.authentication.validtion

interface PasswordValidator {
    fun validatePassword(password: String)
}