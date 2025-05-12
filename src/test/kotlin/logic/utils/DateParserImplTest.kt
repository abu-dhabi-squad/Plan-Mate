package logic.utils

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import java.time.LocalDate
import java.time.format.DateTimeParseException

class DateParserImplTest {

    private lateinit var dateParserImpl: DateParserImpl

    @BeforeEach
    fun setup() {
        dateParserImpl = DateParserImpl()
    }

    @ParameterizedTest
    @CsvSource("2025,4,30", "2025,1,1", "2025,12,31", "2025,2,28")
    fun `parseDateFromString should return object of local date when the input is valid`(
        year: Int,
        month: Int,
        day: Int
    ) {
        // Given
        val date = "$year-$month-$day"

        // When
        val result = dateParserImpl.parseDateFromString(date)

        // Then
        assertThat(result).isEqualTo(LocalDate.of(year, month, day))
    }

    @Test
    fun `getStringFromDate should return correct formatted string`() {
        // Given
        val date = LocalDate.of(2025, 5, 3)

        // When
        val result = dateParserImpl.getStringFromDate(date)

        // Then
        assertThat(result).isEqualTo("2025-5-3")
    }

    @ParameterizedTest
    @ValueSource(strings = ["2020-10-0", "2020-10-32", "2020-13-10", "2020-0-10"])
    fun `should throw DateTimeParseException when the input contains invalid day or month`(date: String) {
        // Given
        // invalid date input

        // When & Then
        assertThrows<DateTimeParseException> {
            dateParserImpl.parseDateFromString(date)
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["2025/4/30", "2025&4&30", "2025a4a30", "2025.4.30"])
    fun `should throw DateTimeParseException when the input contains invalid character`(date: String) {
        // Given
        // invalid date format

        // When & Then
        assertThrows<DateTimeParseException> {
            dateParserImpl.parseDateFromString(date)
        }
    }

    @Test
    fun `getStringFromDate should handle single-digit months and days without padding`() {
        // Given
        val date = LocalDate.of(2025, 1, 2)

        // When
        val result = dateParserImpl.getStringFromDate(date)

        // Then
        assertThat(result).isEqualTo("2025-1-2") // no zero-padding
    }

    @Test
    fun `parseDateFromString and getStringFromDate should be symmetrical`() {
        // Given
        val original = "2025-6-9"

        // When
        val parsed = dateParserImpl.parseDateFromString(original)
        val result = dateParserImpl.getStringFromDate(parsed)

        // Then
        assertThat(result).isEqualTo(original)
    }

    @Test
    fun `parseDateFromString should throw when input is empty`() {
        // Given
        val input = ""

        // When & Then
        assertThrows<DateTimeParseException> {
            dateParserImpl.parseDateFromString(input)
        }
    }

    @Test
    fun `parseDateFromString should throw when input is null-equivalent`() {
        // Given
        val nullLikeInput = "null"

        // When & Then
        assertThrows<DateTimeParseException> {
            dateParserImpl.parseDateFromString(nullLikeInput)
        }
    }
}
