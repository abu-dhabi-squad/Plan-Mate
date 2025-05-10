package logic.validation

import logic.authentication.validtion.CreateUserPasswordValidator
import logic.exceptions.NoLowerCaseInPasswordException
import logic.exceptions.NoNumberInPasswordException
import logic.exceptions.NoSpecialCharsInPasswordException
import logic.exceptions.NoUpperCaseInPasswordException
import logic.exceptions.ShortPasswordException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CreateUserPasswordValidatorTest {

    private lateinit var createUserPasswordValidator: CreateUserPasswordValidator

    @BeforeEach
    fun setup() {
        createUserPasswordValidator = CreateUserPasswordValidator()
    }

    @Test
    fun `should validate successfully when password is contain uppercase, lowercase, special characters, Number and it length more than 7`() {
        // Given
        val password = "Alaa@Kh1"

        // When & Then
        createUserPasswordValidator.validatePassword(password)
    }

    @Test
    fun `should throw exception when password is less than 8 characters`() {
        // Given
        val password = "AK@1a"

        // When & Then
        assertThrows<ShortPasswordException> {
            createUserPasswordValidator.validatePassword(password)
        }
    }

    @Test
    fun `should throw exception when password does not contain uppercase letter`() {
        // Given
        val password = "alaa@123"

        // When & Then
        assertThrows<NoUpperCaseInPasswordException> {
            createUserPasswordValidator.validatePassword(password)
        }
    }

    @Test
    fun `should throw exception when password does not contain lowercase letter`() {
        // Given
        val password = "ALAA@123"

        // When & Then
        assertThrows<NoLowerCaseInPasswordException> {
            createUserPasswordValidator.validatePassword(password)
        }
    }

    @Test
    fun `should throw exception when password does not contain a number`() {
        // Given
        val password = "Alaa@Khaled"

        // When & Then
        assertThrows<NoNumberInPasswordException> {
            createUserPasswordValidator.validatePassword(password)
        }
    }

    @Test
    fun `should throw exception when password does not contain special character`() {
        // Given
        val password = "AlaaKhaled1"

        // When & Then
        assertThrows<NoSpecialCharsInPasswordException> {
            createUserPasswordValidator.validatePassword(password)
        }
    }
}