package logic.validation

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import squad.abudhabi.logic.exceptions.*
import squad.abudhabi.logic.validation.PasswordValidator

class StandardPasswordValidatorTest {

    private lateinit var validator: PasswordValidator

    @BeforeEach
    fun setup() {
        validator = mockk(relaxed = true)
    }

    @Test
    fun `should validate successfully when password is contain uppercase, lowercase, special characters, Number and it length more then 7`() {
        // Given
        val password = "Alaa@Kh1"
        // When and Then
        validator.validatePassword(password)
    }

    @Test
    fun `should throw exception when password is less than 8 characters`() {
        // Given
        val password = "AK@1a"
        every { validator.validatePassword(password) } throws ShortPasswordException()

        // When
        val exception = assertThrows<ShortPasswordException> {
            validator.validatePassword(password)
        }

        // Then
        assertThat(exception).hasMessageThat().isEqualTo("Password must be at least 8 characters long")
    }

    @Test
    fun `should throw exception when password does not contain uppercase letter`() {
        // Given
        val password = "alaa@123"
        every { validator.validatePassword(password) } throws NoUpperCaseInPasswordException()

        // When
        val exception = assertThrows<NoUpperCaseInPasswordException> {
            validator.validatePassword(password)
        }

        // Then
        assertThat(exception).hasMessageThat().isEqualTo("Password must contain at least one uppercase letter")
    }

    @Test
    fun `should throw exception when password does not contain lowercase letter`() {
        // Given
        val password = "ALAA@123"
        every { validator.validatePassword(password) } throws NoLowerCaseInPasswordException()

        // When
        val exception = assertThrows<NoLowerCaseInPasswordException> {
            validator.validatePassword(password)
        }

        // Then
        assertThat(exception).hasMessageThat().isEqualTo("Password must contain at least one lowercase letter")
    }

    @Test
    fun `should throw exception when password does not contain a number`() {
        // Given
        val password = "Alaa@Khaled"
        every { validator.validatePassword(password) } throws NoNumberInPasswordException()

        // When
        val exception = assertThrows<NoNumberInPasswordException> {
            validator.validatePassword(password)
        }

        // Then
        assertThat(exception).hasMessageThat().isEqualTo("Password must contain at least one number")
    }

    @Test
    fun `should throw exception when password does not contain special character`() {
        // Given
        val password = "AlaaKhaled1"
        every { validator.validatePassword(password) } throws NoSpecialCharsInPasswordException()

        // When
        val exception = assertThrows<NoSpecialCharsInPasswordException> {
            validator.validatePassword(password)
        }

        // Then
        assertThat(exception).hasMessageThat().isEqualTo("Password must contain at least one special character")
    }
}
