package logic.authentication.validation

import logic.exceptions.ShortPasswordException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class LoginPasswordValidatorTest {
    private val loginPasswordValidator = LoginPasswordValidator()

    @Test
    fun `should validate successfully when password is contain uppercase, lowercase, special characters, Number and it length more than 7`() {
        // Given
        val password = "12345678"

        // When & Then
        loginPasswordValidator.validatePassword(password)
    }

    @Test
    fun `should throw exception when password is less than 8 characters`() {
        // Given
        val password = "123"

        // When & Then
        assertThrows<ShortPasswordException> {
            loginPasswordValidator.validatePassword(password)
        }
    }
}