import logic.authentication.validation.CreateUserPasswordValidator
import logic.exceptions.ShortPasswordException
import logic.exceptions.NoUpperCaseInPasswordException
import logic.exceptions.NoLowerCaseInPasswordException
import logic.exceptions.NoNumberInPasswordException
import logic.exceptions.NoSpecialCharsInPasswordException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.assertDoesNotThrow

class CreateUserPasswordValidatorTest {

    private val validator = CreateUserPasswordValidator()

    @Test
    fun `should pass validation when password is valid`() {
        // Given
        val password = "Abc123@#"

        // When & Then
        assertDoesNotThrow {
            validator.validatePassword(password)
        }
    }

    @Test
    fun `should throw ShortPasswordException when password is shorter than 8 characters`() {
        // Given
        val password = "A1@bc"

        // When & Then
        assertThrows<ShortPasswordException> {
            validator.validatePassword(password)
        }
    }

    @Test
    fun `should throw NoUpperCaseInPasswordException when password contains no uppercase letter`() {
        // Given
        val password = "abc123@#"

        // When & Then
        assertThrows<NoUpperCaseInPasswordException> {
            validator.validatePassword(password)
        }
    }

    @Test
    fun `should throw NoLowerCaseInPasswordException when password contains no lowercase letter`() {
        // Given
        val password = "ABC123@#"

        // When & Then
        assertThrows<NoLowerCaseInPasswordException> {
            validator.validatePassword(password)
        }
    }

    @Test
    fun `should throw NoNumberInPasswordException when password contains no digit`() {
        // Given
        val password = "Abcdef@#"

        // When & Then
        assertThrows<NoNumberInPasswordException> {
            validator.validatePassword(password)
        }
    }

    @Test
    fun `should throw NoSpecialCharsInPasswordException when password contains no special character`() {
        // Given
        val password = "Abcd1234"

        // When & Then
        assertThrows<NoSpecialCharsInPasswordException> {
            validator.validatePassword(password)
        }
    }
}