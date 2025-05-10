package logic.authentication.validtion

import logic.exceptions.NoLowerCaseInPasswordException
import logic.exceptions.NoNumberInPasswordException
import logic.exceptions.NoSpecialCharsInPasswordException
import logic.exceptions.NoUpperCaseInPasswordException
import logic.exceptions.ShortPasswordException
import logic.validation.PasswordValidator

class CreateUserPasswordValidator : PasswordValidator {

    override fun validatePassword(password: String) {

        if (password.length < 8)
            throw ShortPasswordException()

        if (!password.any { it.isUpperCase() })
            throw NoUpperCaseInPasswordException()

        if (!password.any { it.isLowerCase() })
            throw NoLowerCaseInPasswordException()

        if (!password.any { it.isDigit() })
            throw NoNumberInPasswordException()

        if (!password.any { it in SYMBOLS })
            throw NoSpecialCharsInPasswordException()

    }

    companion object { private const val SYMBOLS = "@$&*#_-.()"}
}