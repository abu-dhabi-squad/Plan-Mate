package logic.authentication.validtion

import logic.exceptions.ShortPasswordException
import logic.validation.PasswordValidator

class LoginPasswordValidator : PasswordValidator {
    override fun validatePassword(password: String) {
        if (password.length < 8)
            throw ShortPasswordException()
    }
}