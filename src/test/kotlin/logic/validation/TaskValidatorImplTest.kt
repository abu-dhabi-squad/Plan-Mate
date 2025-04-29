package logic.validation

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import squad.abudhabi.logic.exceptions.InvalidDateException
import squad.abudhabi.logic.exceptions.InvalidTaskDateException
import squad.abudhabi.logic.validation.DateParser
import squad.abudhabi.logic.validation.DateValidator
import squad.abudhabi.logic.validation.TaskValidatorImpl
import java.time.LocalDate

class TaskValidatorImplTest {
    private lateinit var taskValidator: TaskValidatorImpl
    private lateinit var dateValidator: DateValidator
    private lateinit var dateParser: DateParser
    @BeforeEach
    fun setup() {
        dateValidator = mockk(relaxed = true)
        dateParser = mockk(relaxed = true)
        taskValidator = TaskValidatorImpl(dateValidator, dateParser)
    }

    @Test
    fun `should not throw exception when task data is valid`() {
        // given
        val startData = "2025-04-01"
        val endDate = "2025-05-01"
        // when && then
        taskValidator.validateOrThrow(startData, endDate)
    }

    @ParameterizedTest
    @CsvSource(
        "'',2025-05-01",
        "01-04-2025,2025-05-01",
        "'',''"
    )
    fun `should throw InvalidDateException when task data is invalid`(
        startData: String,
        endDate: String
    ) {
        every { dateValidator.validateDateOrThrow(startData)} throws InvalidDateException()
        every { dateValidator.validateDateOrThrow(endDate)} throws InvalidDateException()
        every { dateParser.parseDateFromString(startData)} throws InvalidDateException()
        every { dateParser.parseDateFromString(endDate)} throws InvalidDateException()
        // when && then
        assertThrows<InvalidTaskDateException> {
            taskValidator.validateOrThrow(startData, endDate)
        }
    }

    @Test
    fun `should throw InvalidTaskDateException when end date is before start date`(){
        // given
        val startData = "2025-05-01"
        val endDate = "2025-04-01"
        every { dateParser.parseDateFromString(startData)} returns  LocalDate.parse(startData)
        every { dateParser.parseDateFromString(endDate)} returns  LocalDate.parse(endDate)
        // when
        assertThrows<InvalidTaskDateException> {
            taskValidator.validateOrThrow(startData, endDate)
        }
    }
}