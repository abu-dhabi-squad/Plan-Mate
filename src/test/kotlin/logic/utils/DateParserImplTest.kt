package logic.utils

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import presentation.logic.utils.DateParserImpl
import java.time.LocalDate
import java.time.format.DateTimeParseException
import kotlin.test.assertFailsWith

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
        val date = "$year-$month-$day"
        val result = dateParserImpl.parseDateFromString(date)
        assertThat(result).isEqualTo(LocalDate.of(year, month, day))
    }

    @Test
    fun `getStringFromDate should return correct formatted string`() {
        val date = LocalDate.of(2025, 5, 3)
        val result = dateParserImpl.getStringFromDate(date)
        assertThat(result).isEqualTo("2025-5-3")
    }

    @ParameterizedTest
    @ValueSource(strings = ["2020-10-0", "2020-10-32", "2020-13-10", "2020-0-10"])
    fun `should throw DateTimeParseException when the input contains invalid day or month`(date: String) {
        assertFailsWith<DateTimeParseException> {
            dateParserImpl.parseDateFromString(date)
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["2025/4/30", "2025&4&30", "2025a4a30", "2025.4.30"])
    fun `should throw DateTimeParseException when the input contains invalid character`(date: String) {
        assertFailsWith<DateTimeParseException> {
            dateParserImpl.parseDateFromString(date)
        }
    }

    @Test
    fun `getStringFromDate should handle single-digit months and days without padding`() {
        val date = LocalDate.of(2025, 1, 2)
        val result = dateParserImpl.getStringFromDate(date)
        assertThat(result).isEqualTo("2025-1-2") // no zero-padding in pattern "yyyy-M-d"
    }

    @Test
    fun `parseDateFromString and getStringFromDate should be symmetrical`() {
        val original = "2025-6-9"
        val parsed = dateParserImpl.parseDateFromString(original)
        val result = dateParserImpl.getStringFromDate(parsed)
        assertThat(result).isEqualTo(original)
    }

    @Test
    fun `parseDateFromString should throw when input is empty`() {
        assertFailsWith<DateTimeParseException> {
            dateParserImpl.parseDateFromString("")
        }
    }

    @Test
    fun `parseDateFromString should throw when input is null-equivalent`() {
        val nullLikeInput = "null"
        assertFailsWith<DateTimeParseException> {
            dateParserImpl.parseDateFromString(nullLikeInput)
        }
    }
}
