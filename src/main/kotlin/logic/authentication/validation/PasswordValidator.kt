package logic.authentication.validation

interface PasswordValidator {
    fun validatePassword(password: String)
}