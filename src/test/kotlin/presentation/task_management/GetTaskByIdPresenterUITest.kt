package presentation.task_management

import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import squad.abudhabi.logic.model.Task
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
    fun `should display error when task ID is blank`(taskID: String?) = runTest{
        // Given
        coEvery { inputReader.readString() } returns taskID
        // When
        presenter.launchUi()
        // Then
        coVerify { printer.displayLn("Task ID cannot be empty.") }
    }

    @Test
    fun `should display task details when task is found`() = runTest{
        // Given
        val task = Task(
            title = "Task 1",
            description = "Some description",
            startDate = LocalDate.of(2025, 5, 1),
            endDate = LocalDate.of(2025, 5, 10),
            projectId = "p1",
            stateId = "s1",
            userName = "John Doe"
        )

        coEvery { inputReader.readString() } returns "t1"
        coEvery { getTaskByIdUseCase("t1") } returns task
        // When
        presenter.launchUi()
        // Then
        coVerify { printer.displayLn("Task Found:") }
        coVerify { printer.displayLn("Title: ${task.title}") }
        coVerify { printer.displayLn("Description: ${task.description}") }
        coVerify { printer.displayLn("Start Date: ${task.startDate}") }
        coVerify { printer.displayLn("End Date: ${task.endDate}") }
        coVerify { printer.displayLn("Project ID: ${task.projectId}") }
        coVerify { printer.displayLn("State ID: ${task.stateId}") }
        coVerify { printer.displayLn("Assigned to: ${task.userName}") }
    }

    @Test
    fun `should display error message when task is not found`() = runTest{
        // Given
        coEvery { inputReader.readString() } returns "invalid-id"
        coEvery { getTaskByIdUseCase("invalid-id") } throws NoSuchElementException("Task not found")
        // When
        presenter.launchUi()
        // Then
        coVerify { printer.displayLn("Failed to retrieve task: Task not found") }
    }
}
