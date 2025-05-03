package logic.validation

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
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
        "2025/05/03 3:30 PM, 2025,5,3,15,30",
        "2024/12/01 12:00 AM, 2024,12,1,0,0",
        "2024/12/01 12:00 PM, 2024,12,1,12,0",
        "2023/01/31 9:45 AM, 2023,1,31,9,45"
    )
    fun `parseDateFromString should return correct LocalDateTime`(
        input: String,
        year: Int,
        month: Int,
        day: Int,
        hour: Int,
        minute: Int
    ) {
        val expected = LocalDateTime.of(year, month, day, hour, minute)
        val actual = parser.parseDateFromString(input)
        assertThat(actual).isEqualTo(expected)
    }

    @ParameterizedTest
    @CsvSource(
        "2025,5,3,15,30,2025/05/03 3:30 PM",
        "2024,12,1,0,0,2024/12/01 12:00 AM",
        "2024,12,1,12,0,2024/12/01 12:00 PM"
    )
    fun `getStringFromDate should return correctly formatted string`(
        year: Int,
        month: Int,
        day: Int,
        hour: Int,
        minute: Int,
        expected: String
    ) {
        val input = LocalDateTime.of(year, month, day, hour, minute)
        val result = parser.getStringFromDate(input)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `parseDateFromString should throw IllegalArgumentException for incorrect format`() {
        val invalid = "2025-05-03 15:30"
        val exception = assertFailsWith<IllegalArgumentException> {
            parser.parseDateFromString(invalid)
        }
        assertThat(exception.message).contains("Invalid date format")
    }

    @Test
    fun `parseDateFromString should throw for invalid values like wrong hour or day`() {
        val invalidDate = "2025/02/30 13:00 PM" //  13 PM  doesn't exist
        assertFailsWith<IllegalArgumentException> {
            parser.parseDateFromString(invalidDate)
        }
    }

    @Test
    fun `getStringFromDate then parseDateFromString should be symmetrical`() {
        val original = LocalDateTime.of(2025, 5, 3, 15, 30)
        val stringified = parser.getStringFromDate(original)
        val parsed = parser.parseDateFromString(stringified)
        assertThat(parsed).isEqualTo(original)
    }

    @Test
    fun `parseDateFromString should trim leading or trailing whitespace`() {
        val input = " 2025/05/03 3:30 PM "
        val result = parser.parseDateFromString(input.trim())
        assertThat(result).isEqualTo(LocalDateTime.of(2025, 5, 3, 15, 30))
    }
}
