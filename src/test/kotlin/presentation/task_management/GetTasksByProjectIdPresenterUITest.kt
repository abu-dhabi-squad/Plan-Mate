package presentation.task_management

import helper.createProject
import helper.createState
import helper.createTask
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import logic.project.GetAllProjectsUseCase
import logic.task.GetTasksByProjectIdUseCase
import presentation.ui_io.InputReader
import presentation.ui_io.Printer
import java.time.LocalDate

class GetTasksByProjectIdPresenterUITest {

    private val printer = mockk<Printer>(relaxed = true)
    private val inputReader = mockk<InputReader>(relaxed = true)
    private val getAllProjectsUseCase = mockk<GetAllProjectsUseCase>()
    private val getTasksByProjectIdUseCase = mockk<GetTasksByProjectIdUseCase>()

    private lateinit var presenter: GetTasksByProjectIdPresenterUI

    @BeforeEach
    fun setUp() {
        presenter = GetTasksByProjectIdPresenterUI(
            printer,
            inputReader,
            getAllProjectsUseCase,
            getTasksByProjectIdUseCase
        )
    }

    @Test
    fun `should display error when loading projects fails`() = runTest{
        // Given
        every { getAllProjectsUseCase() } throws RuntimeException("DB error")
        // When
        presenter.launchUi()
        // Then
        verify { printer.displayLn("Failed to load projects: DB error") }
    }

    @Test
    fun `should display message when no projects are available`() = runTest{
        // Given
        every { getAllProjectsUseCase() } returns emptyList()
        // When
        presenter.launchUi()
        // Then
        verify { printer.displayLn("No projects available.") }
    }

    @Test
    fun `should display error when loading tasks fails`() = runTest{
        // Given
        val project = createProject("p1", name = "Project A")
        every { getAllProjectsUseCase() } returns listOf(project)
        every { inputReader.readInt() } returns 1
        every { getTasksByProjectIdUseCase("p1") } throws RuntimeException("Network issue")
        // When
        presenter.launchUi()
        // Then
        verify { printer.displayLn("Failed to load tasks: Network issue") }
    }

    @Test
    fun `should display message when no tasks found for selected project`() = runTest{
        // Given
        val project =
            createProject("p1", name = "Project A", states = listOf(createState(id = "s1")))
        every { getAllProjectsUseCase() } returns listOf(project)
        every { inputReader.readInt() } returns 1
        every { getTasksByProjectIdUseCase("p1") } returns emptyList()
        // When
        presenter.launchUi()
        // Then
        verify { printer.displayLn("No tasks found in 'Project A'.") }
    }

    @Test
    fun `should show project list and task list successfully`() = runTest{
        // Given
        val project = createProject("p1", name = "Project A", states = listOf(createState(id = "s1")))
        val task = createTask(
            id = "t1",
            title = "Fix Bug",
            description = "Resolve login issue",
            startDate = LocalDate.of(2025, 5, 1),
            endDate = LocalDate.of(2025, 5, 2),
            projectId = "p1",
            stateId = "s1",
            userName = "Alice"
        )
        every { getAllProjectsUseCase() } returns listOf(project)
        every { inputReader.readInt() } returns 1
        every { getTasksByProjectIdUseCase("p1") } returns listOf(task)

        // When
        presenter.launchUi()

        // Then
        verifySequence {
            printer.displayLn("Available Projects:")
            printer.displayLn("1. Project A")
            printer.display("Enter project number:")
            printer.displayLn("Tasks in Project:")
            printer.display("""
            1. Fix Bug
               ↳ Description: Resolve login issue
               ↳ Start: 2025-05-01, End: 2025-05-02
               ↳ Assigned to: Alice
               ↳ State ID: s1
        """.trimIndent())
        }
    }
    @ParameterizedTest
    @CsvSource("2,1", "    ,1", "null,1", nullValues = ["null"])
    fun `should prompt again if invalid project number is entered`(
        firstAttemptEnterNumber: Int?,
        secondAttemptEnterIndex: Int
    ) = runTest{
        // Given
        val project =
            createProject("p1", name = "Project A", states = listOf(createState(id = "s1")))
        every { getAllProjectsUseCase() } returns listOf(project)
        every { inputReader.readInt() } returnsMany listOf(firstAttemptEnterNumber,secondAttemptEnterIndex)
        every { getTasksByProjectIdUseCase("p1") } returns emptyList()
        // When
        presenter.launchUi()
        // Then
        verify { printer.displayLn("Please enter a valid number between 1 and 1.") }
        verify { printer.displayLn("No tasks found in 'Project A'.") }
    }
}
