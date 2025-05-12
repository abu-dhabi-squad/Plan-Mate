import logic.authentication.validation.CreateUserPasswordValidator
import logic.exceptions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.assertDoesNotThrow

class CreateUserPasswordValidatorTest {

    private val validator = CreateUserPasswordValidator()

    @Test
    fun `should pass validation when password is valid`() {
        // given
        val password = "Abc123@#"

        // when & then
        assertDoesNotThrow {
            validator.validatePassword(password)
        }
    }

    @Test
    fun `should throw ShortPasswordException when password is shorter than 8 characters`() {
        // given
        val password = "A1@bc"

        // when & then
        assertThrows<ShortPasswordException> {
            validator.validatePassword(password)
        }
    }

    @Test
    fun `should throw NoUpperCaseInPasswordException when password contains no uppercase letter`() {
        // given
        val password = "abc123@#"

        // when & then
        assertThrows<NoUpperCaseInPasswordException> {
            validator.validatePassword(password)
        }
    }

    @Test
    fun `should throw NoLowerCaseInPasswordException when password contains no lowercase letter`() {
        // given
        val password = "ABC123@#"

        // when & then
        assertThrows<NoLowerCaseInPasswordException> {
            validator.validatePassword(password)
        }
    }

    @Test
    fun `should throw NoNumberInPasswordException when password contains no digit`() {
        // given
        val password = "Abcdef@#"

        // when & then
        assertThrows<NoNumberInPasswordException> {
            validator.validatePassword(password)
        }
    }

    @Test
    fun `should throw NoSpecialCharsInPasswordException when password contains no special character`() {
        // given
        val password = "Abcd1234"

        // when & then
        assertThrows<NoSpecialCharsInPasswordException> {
            validator.validatePassword(password)
        }
    }
}