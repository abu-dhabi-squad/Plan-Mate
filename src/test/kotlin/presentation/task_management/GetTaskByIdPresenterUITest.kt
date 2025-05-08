package presentation.task_management

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import logic.model.Task
import logic.task.GetTaskByIdUseCase
import presentation.ui_io.InputReader
import presentation.ui_io.Printer
import java.time.LocalDate

class GetTaskByIdPresenterUITest {

    private val printer = mockk<Printer>(relaxed = true)
    private val inputReader = mockk<InputReader>(relaxed = true)
    private val getTaskByIdUseCase = mockk<GetTaskByIdUseCase>()

    private lateinit var presenter: GetTaskByIdPresenterUI

    @BeforeEach
    fun setup() {
        presenter = GetTaskByIdPresenterUI(printer, inputReader, getTaskByIdUseCase)
    }

    @ParameterizedTest
    @CsvSource("''", "'  '", "null", nullValues = ["null"])
    fun `should display error when task ID is blank`(taskID: String?) {
        // Given
        every { inputReader.readString() } returns taskID
        // When
        presenter.launchUi()
        // Then
        verify { printer.displayLn("Task ID cannot be empty.") }
    }

    @Test
    fun `should display task details when task is found`() {
        // Given
        val task = Task(
            id = "t1",
            title = "Task 1",
            description = "Some description",
            startDate = LocalDate.of(2025, 5, 1),
            endDate = LocalDate.of(2025, 5, 10),
            projectId = "p1",
            stateId = "s1",
            userName = "John Doe"
        )

        every { inputReader.readString() } returns "t1"
        every { getTaskByIdUseCase("t1") } returns task
        // When
        presenter.launchUi()
        // Then
        verify { printer.displayLn("Task Found:") }
        verify { printer.displayLn("Title: ${task.title}") }
        verify { printer.displayLn("Description: ${task.description}") }
        verify { printer.displayLn("Start Date: ${task.startDate}") }
        verify { printer.displayLn("End Date: ${task.endDate}") }
        verify { printer.displayLn("Project ID: ${task.projectId}") }
        verify { printer.displayLn("State ID: ${task.stateId}") }
        verify { printer.displayLn("Assigned to: ${task.userName}") }
    }

    @Test
    fun `should display error message when task is not found`() {
        // Given
        every { inputReader.readString() } returns "invalid-id"
        every { getTaskByIdUseCase("invalid-id") } throws NoSuchElementException("Task not found")
        // When
        presenter.launchUi()
        // Then
        verify { printer.displayLn("Failed to retrieve task: Task not found") }
    }
}
