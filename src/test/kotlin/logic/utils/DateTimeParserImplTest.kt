package logic.utils

import com.google.common.truth.Truth.assertThat
import logic.exceptions.DateFormatException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import presentation.logic.utils.DateTimeParserImpl
import java.time.LocalDateTime
import kotlin.test.assertFailsWith

class DateTimeParserImplTest {

    private lateinit var parser: DateTimeParserImpl

    @BeforeEach
    fun setup() {
        parser = DateTimeParserImpl()
    }

    @ParameterizedTest
    @CsvSource(
        "2025/05/03 15:30, 2025,5,3,15,30",
        "2024/12/01 00:00, 2024,12,1,0,0",
        "2024/12/01 12:00, 2024,12,1,12,0",
        "2023/01/31 09:45, 2023,1,31,9,45"
    )
    fun `parseDateFromString should return correct LocalDateTime`(
        input: String,
        year: Int,
        month: Int,
        day: Int,
        hour: Int,
        minute: Int
    ) {
        // Given
        val expected = LocalDateTime.of(year, month, day, hour, minute)

        // When
        val actual = parser.parseDateFromString(input)

        // Then
        assertThat(actual).isEqualTo(expected)
    }

    @ParameterizedTest
    @CsvSource(
        "2025,5,3,15,30,2025/5/3 15:30",
        "2024,12,1,0,0,2024/12/1 0:00",
        "2024,12,1,12,0,2024/12/1 12:00"
    )
    fun `getStringFromDate should return correctly formatted string`(
        year: Int,
        month: Int,
        day: Int,
        hour: Int,
        minute: Int,
        expected: String
    ) {
        // Given
        val input = LocalDateTime.of(year, month, day, hour, minute)

        // When
        val result = parser.getStringFromDate(input)

        // Then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `parseDateFromString should throw DateFormatException for incorrect format`() {
        // Given
        val invalid = "2025-05-03 15:30" // wrong separator

        // When
        val exception = assertFailsWith<DateFormatException> {
            parser.parseDateFromString(invalid)
        }

        // Then
        assertThat(exception.message).contains("Invalid date format")
    }

    @Test
    fun `parseDateFromString should throw for invalid values like impossible date`() {
        // Given
        val invalidDate = "2025/02/32 13:00"

        // When & Then
        assertThrows<DateFormatException> {
            parser.parseDateFromString(invalidDate)
        }
    }

    @Test
    fun `getStringFromDate then parseDateFromString should be symmetrical`() {
        // Given
        val original = LocalDateTime.of(2025, 5, 3, 15, 30)

        // When
        val stringified = parser.getStringFromDate(original)
        val parsed = parser.parseDateFromString(stringified)

        // Then
        assertThat(parsed).isEqualTo(original)
    }

    @Test
    fun `parseDateFromString should trim leading or trailing whitespace`() {
        // Given
        val input = " 2025/05/03 15:30 "

        // When
        val result = parser.parseDateFromString(input.trim())

        // Then
        assertThat(result).isEqualTo(LocalDateTime.of(2025, 5, 3, 15, 30))
    }

    @Test
    fun `parseDateFromString should throw for hour beyond 23`() {
        // Given
        val input = "2025/05/03 24:01"

        // When & Then
        assertThrows<DateFormatException> {
            parser.parseDateFromString(input)
        }
    }

    @Test
    fun `parseDateFromString should throw for minute beyond 59`() {
        // Given
        val input = "2025/05/03 15:60"

        // When & Then
        assertThrows<DateFormatException> {
            parser.parseDateFromString(input)
        }
    }
}