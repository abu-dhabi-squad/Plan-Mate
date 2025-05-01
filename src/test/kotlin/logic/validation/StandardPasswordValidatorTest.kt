package logic.validation

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import squad.abudhabi.logic.exceptions.*
import squad.abudhabi.logic.validation.StandardPasswordValidator

class StandardPasswordValidatorTest {

    private lateinit var standardPasswordValidator: StandardPasswordValidator

    @BeforeEach
    fun setup() {
        standardPasswordValidator = StandardPasswordValidator()
    }

    @Test
    fun `should validate successfully when password is contain uppercase, lowercase, special characters, Number and it length more then 7`() {
        // given
        val password = "Alaa@Kh1"

        // when & then
        standardPasswordValidator.validatePassword(password)
    }

    @Test
    fun `should throw exception when password is less than 8 characters`() {
        // given
        val password = "AK@1a"

        // when & then
        assertThrows<ShortPasswordException> {
            standardPasswordValidator.validatePassword(password)
        }
    }

    @Test
    fun `should throw exception when password does not contain uppercase letter`() {
        // given
        val password = "alaa@123"

        // when & then
        assertThrows<NoUpperCaseInPasswordException> {
            standardPasswordValidator.validatePassword(password)
        }
    }

    @Test
    fun `should throw exception when password does not contain lowercase letter`() {
        // given
        val password = "ALAA@123"

        // when & then
        assertThrows<NoLowerCaseInPasswordException> {
            standardPasswordValidator.validatePassword(password)
        }
    }

    @Test
    fun `should throw exception when password does not contain a number`() {
        // given
        val password = "Alaa@Khaled"

        // when & then
        assertThrows<NoNumberInPasswordException> {
            standardPasswordValidator.validatePassword(password)
        }
    }

    @Test
    fun `should throw exception when password does not contain special character`() {
        // given
        val password = "AlaaKhaled1"

        // when & then
        assertThrows<NoSpecialCharsInPasswordException> {
            standardPasswordValidator.validatePassword(password)
        }
    }
}
