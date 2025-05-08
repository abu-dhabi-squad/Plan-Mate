package logic.validation

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import logic.exceptions.InvalidDateFormatException
import logic.exceptions.InvalidYearException
import java.time.LocalDate
import kotlin.test.Test

class DateValidatorImplTest {
    private lateinit var dateParserInterface: DateParser
    private lateinit var dateValidatorImpl: DateValidatorImpl

    @BeforeEach
    fun setup() {
        dateParserInterface = mockk(relaxed = true)
        dateValidatorImpl = DateValidatorImpl(dateParserInterface)
    }

    @Test
    fun `should not throw exception when date is valid`() {
        // Given
        val date = "2020-2-10"
        every { dateParserInterface.parseDateFromString(date) } returns LocalDate.of(2020, 2, 10)

        // When
        dateValidatorImpl.validateDateOrThrow(date)

        // Then
        verify(exactly = 1) { dateParserInterface.parseDateFromString(date) }
    }

    @Test
    fun `should throw InvalidDateFormatException when invalid date format provided`() {
        // Given
        every { dateParserInterface.parseDateFromString(any()) } throws InvalidDateFormatException()

        // When
        assertThrows<InvalidDateFormatException> {
            dateValidatorImpl.validateDateOrThrow("2000/10-10")
        }
    }
    @ParameterizedTest
    @ValueSource(ints = [1999, 1900, 1000, -1000])
    fun `should throw InvalidYearException when year from the is very old`(year:Int) {
        // Given
        val invalidOldYear = year
        val date = "$invalidOldYear-10-10"
        every { dateParserInterface.parseDateFromString(date) } returns LocalDate.of(
            invalidOldYear,
            10,
            10
        )

        // When & Then
        assertThrows<InvalidYearException> {
            dateValidatorImpl.validateDateOrThrow(date)
        }
    }

}