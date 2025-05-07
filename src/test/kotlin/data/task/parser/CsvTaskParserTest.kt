package data.task.parser

import com.google.common.truth.Truth
import helper.createTask
import io.mockk.every
import io.mockk.mockk
import logic.validation.DateParserImpl
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import java.time.format.DateTimeParseException
import java.util.UUID

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
        val uuid=UUID.randomUUID()
        val csvLine = "${uuid},Test User,d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a,1,Test Task,Test Description,2023-01-01,2023-01-01"
        val task = createTask(
            id = uuid,
            userName = "Test User",
            projectId = "d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a",
            stateId = "1",
            title = "Test Task",
            description = "Test Description",
            startDate = LocalDate.of(2023, 1, 1),
            endDate = LocalDate.of(2023, 1, 1),
        )
        every { dateParserImpl.parseDateFromString(any()) } returns task.startDate

        // When
        val result = csvTaskParser.getTaskFromCsvLine(csvLine)

        // Then
        Truth.assertThat(result).isEqualTo(task)
    }
    @Test
    fun `getTaskFromCsvLine should throws DateTimeParseException when date in csv line is not valid`() {
        // Given
        val uuid=UUID.randomUUID()
        val csvLine = "${uuid},Test User,d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a,1,Test Task,Test Description,2023-20-20,2023-20-20"
        every { dateParserImpl.parseDateFromString(any()) } throws DateTimeParseException("", "", 0)

        // When && Then
        assertThrows<DateTimeParseException> { csvTaskParser.getTaskFromCsvLine(csvLine) }
    }

    @Test
    fun `getCsvLineFromTask should return csv task line when task is valid`() {
        // Given
        val csvLine = "d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a,Test User,d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a,1,Test Task,Test Description,2023-01-01,2023-01-01"
        val task = createTask(
            id=UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"),
            userName = "Test User",
            projectId = "d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a",
            stateId = "1",
            title = "Test Task",
            description = "Test Description",
            startDate = LocalDate.of(2023, 1, 1),
            endDate = LocalDate.of(2023, 1, 1),
        )
        every { dateParserImpl.getStringFromDate(any()) } returns "2023-01-01"

        // When
        val result = csvTaskParser.getCsvLineFromTask(task)

        // Then
        Truth.assertThat(result).isEqualTo(csvLine)
    }
}
