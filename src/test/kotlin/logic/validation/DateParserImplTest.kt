package logic.validation

import com.google.common.truth.Truth
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import squad.abudhabi.logic.validation.DateParserImpl
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
    @CsvSource("2025,4,30", "2025,1,1", "2025,12,31")
    fun `parseDateFromString should return object of local date when the input is valid`(
        year: Int,
        month: Int,
        day: Int
    ) {
        //given
        val date = "$year-$month-$day"
        //when
        val res = dateParserImpl.parseDateFromString(date)
        //then
        Truth.assertThat(res).isEqualTo(LocalDate.of(year, month, day))
    }

    @ParameterizedTest
    @ValueSource(strings = [
        "2020-10-0",
        "2020-10-32",
        "2020-13-10",
        "2020-0-10"
    ])
    fun `should throw DateTimeParseException when the input contains invalid day or month`(date: String) {
        // when & then
        assertFailsWith<DateTimeParseException> {
            dateParserImpl.parseDateFromString(date)
        }
    }

    @ParameterizedTest
    @ValueSource(strings = [
        "2025/4/30",
        "2025&4&30",
        "2025a4a30",
        "2025.4.30"
    ])
    fun `should throw DateTimeParseException when the input contains invalid character`(date: String) {
        // when & then
        assertFailsWith<DateTimeParseException> {
            dateParserImpl.parseDateFromString(date)
        }
    }


    @Test
    fun `parseDateFromString should return date of the last day in month when the input' day is more than the last day of the month`() {
        //given
        val date = "2025-4-31"
        val res = LocalDate.of(2025, 4, 30)
        //when & then
        Truth.assertThat(dateParserImpl.parseDateFromString(date)).isEqualTo(res)
    }

}