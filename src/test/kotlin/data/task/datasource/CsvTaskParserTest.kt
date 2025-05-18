package data.task.datasource

import com.google.common.truth.Truth.assertThat
import data.task.datasource.csv.CsvTaskParser
import helper.createTask
import io.mockk.every
import io.mockk.mockk
import logic.utils.DateParserImpl
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import kotlinx.datetime.LocalDate
import logic.exceptions.DateFormatException
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class CsvTaskParserTest {
    private lateinit var dateParserImpl: DateParserImpl
    private lateinit var csvTaskParser: CsvTaskParser

    @BeforeEach
    fun setup() {
        dateParserImpl = mockk(relaxed = true)
        csvTaskParser = CsvTaskParser(dateParserImpl)
    }

    @Test
    fun `getTaskFromCsvLine should return task when csv line is valid`() {
        // Given
        val uuid=Uuid.random()
        val csvLine = "${uuid},Test User,d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a,d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1b,Test Task,Test Description,2023-01-01,2023-01-01"
        val task = createTask(
            id = uuid,
            userName = "Test User",
            projectId = Uuid.parse("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"),
            stateId = Uuid.parse("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1b"),
            title = "Test Task",
            description = "Test Description",
            startDate = LocalDate(2023, 1, 1),
            endDate = LocalDate(2023, 1, 1),
        )
        every { dateParserImpl.parseDateFromString(any()) } returns task.startDate

        // When
        val result = csvTaskParser.getTaskFromCsvLine(csvLine)

        // Then
        assertThat(result).isEqualTo(task)
    }
    @Test
    fun `getTaskFromCsvLine should throws DateTimeParseException when date in csv line is not valid`() {
        // Given
        val uuid=Uuid.random()
        val csvLine = "${uuid},Test User,d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a,d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1b,Test Task,Test Description,2023-20-20,2023-20-20"
        every { dateParserImpl.parseDateFromString(any()) } throws DateFormatException("", "")

        // When && Then
        assertThrows<DateFormatException> { csvTaskParser.getTaskFromCsvLine(csvLine) }
    }

    @Test
    fun `getCsvLineFromTask should return csv task line when task is valid`() {
        // Given
        val csvLine = "d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a,Test User,d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a,d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1b,Test Task,Test Description,2023-01-01,2023-01-01"
        val task = createTask(
            id=Uuid.parse("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"),
            userName = "Test User",
            projectId = Uuid.parse("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"),
            stateId = Uuid.parse("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1b"),
            title = "Test Task",
            description = "Test Description",
            startDate = LocalDate(2023, 1, 1),
            endDate = LocalDate(2023, 1, 1),
        )
        every { dateParserImpl.getStringFromDate(any()) } returns "2023-01-01"

        // When
        val result = csvTaskParser.getCsvLineFromTask(task)

        // Then
        assertThat(result).isEqualTo(csvLine)
    }
}
