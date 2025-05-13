package logic.authentication.validation

import logic.exceptions.ShortPasswordException
import logic.authentication.validtion.PasswordValidator

class LoginPasswordValidator : PasswordValidator {
    override fun validatePassword(password: String) {
        if (password.length < 8)
            throw ShortPasswordException()
    }
}