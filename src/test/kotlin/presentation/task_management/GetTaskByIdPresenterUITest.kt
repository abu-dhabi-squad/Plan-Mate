package presentation.task_management

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import squad.abudhabi.logic.model.Task
import squad.abudhabi.logic.task.GetTaskByIdUseCase
import squad.abudhabi.presentation.ui_io.InputReader
import squad.abudhabi.presentation.ui_io.Printer
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
    @CsvSource("''","'  '","null", nullValues = ["null"])
    fun `should display error when task ID is blank`(taskID: String?) {
        every { inputReader.readString() } returns taskID

        presenter.launchUi()

        verify { printer.display("❌ Task ID cannot be empty.") }
    }

    @Test
    fun `should display task details when task is found`() {
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

        presenter.launchUi()

        verify { printer.display("✅ Task Found:") }
        verify { printer.display("Title: ${task.title}") }
        verify { printer.display("Description: ${task.description}") }
        verify { printer.display("Start Date: ${task.startDate}") }
        verify { printer.display("End Date: ${task.endDate}") }
        verify { printer.display("Project ID: ${task.projectId}") }
        verify { printer.display("State ID: ${task.stateId}") }
        verify { printer.display("Assigned to: ${task.userName}") }
    }

    @Test
    fun `should display error message when task is not found`() {
        every { inputReader.readString() } returns "invalid-id"
        every { getTaskByIdUseCase("invalid-id") } throws NoSuchElementException("Task not found")

        presenter.launchUi()

        verify { printer.display("❌ Failed to retrieve task: Task not found") }
    }
}
