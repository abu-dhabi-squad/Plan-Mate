package logic.authentication.validtion

import logic.exceptions.ShortPasswordException
import presentation.logic.authentication.validtion.PasswordValidator

class LoginPasswordValidator : PasswordValidator {
    override fun validatePassword(password: String) {
        if (password.length < 8)
            throw ShortPasswordException()
    }
}