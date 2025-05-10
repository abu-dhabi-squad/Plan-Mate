package presentation.taskmanagement

import helper.createProject
import helper.createState
import helper.createTask
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import logic.project.GetAllProjectsUseCase
import logic.task.GetTasksByProjectIdUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import presentation.io.InputReader
import presentation.io.Printer
import java.time.LocalDate
import java.util.*

class GetTasksByProjectIdUITest {
    private val printer = mockk<Printer>(relaxed = true)
    private val inputReader = mockk<InputReader>(relaxed = true)
    private val getAllProjectsUseCase = mockk<GetAllProjectsUseCase>()
    private val getTasksByProjectIdUseCase = mockk<GetTasksByProjectIdUseCase>()

    private lateinit var presenter: GetTasksByProjectIdUI

    @BeforeEach
    fun setUp() {
        presenter = GetTasksByProjectIdUI(
            printer,
            inputReader,
            getAllProjectsUseCase,
            getTasksByProjectIdUseCase
        )
    }

    @Test
    fun `should display error when loading projects fails`() = runTest {
        // Given
        coEvery { getAllProjectsUseCase() } throws RuntimeException("DB error")
        // When
        presenter.launchUi()
        // Then
        verify { printer.displayLn("\nFailed to load projects: DB error") }
    }

    @Test
    fun `should display message when no projects are available`() = runTest {
        // Given
        coEvery { getAllProjectsUseCase() } returns emptyList()
        // When
        presenter.launchUi()
        // Then
        verify { printer.displayLn("\nNo projects available.") }
    }

    @Test
    fun `should display error when loading tasks fails`() = runTest {
        // Given
        val project = createProject(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), name = "Project A")
        coEvery { getAllProjectsUseCase() } returns listOf(project)
        coEvery { inputReader.readInt() } returns 1
        coEvery { getTasksByProjectIdUseCase(project.id) } throws RuntimeException("Network issue")
        // When
        presenter.launchUi()
        // Then
        verify { printer.displayLn("\nFailed to load tasks: Network issue") }
    }

    @Test
    fun `should display message when no tasks found for selected project`() = runTest {
        // Given
        val project =
            createProject(
                UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"),
                name = "Project A",
                states = listOf(createState(id = UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1b")))
            )
        coEvery { getAllProjectsUseCase() } returns listOf(project)
        coEvery { inputReader.readInt() } returns 1
        coEvery { getTasksByProjectIdUseCase(project.id) } returns emptyList()
        // When
        presenter.launchUi()
        // Then
        verify { printer.displayLn("\nNo tasks found in 'Project A'.") }
    }

    @Test
    fun `should show project list and task list successfully`() = runTest {
        // Given
        val uuid = UUID.randomUUID()
        val project = createProject(
            UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"),
            name = "Project A",
            states = listOf(createState(id = UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1b")))
        )
        val task = createTask(
            id = uuid,
            title = "Fix Bug",
            description = "Resolve login issue",
            startDate = LocalDate.of(2025, 5, 1),
            endDate = LocalDate.of(2025, 5, 2),
            projectId = UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"),
            stateId = UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1b"),
            userName = "Alice"
        )
        coEvery { getAllProjectsUseCase() } returns listOf(project)
        coEvery { inputReader.readInt() } returns 1
        coEvery { getTasksByProjectIdUseCase(project.id) } returns listOf(task)

        // When
        presenter.launchUi()

        // Then
        verify {
            printer.displayLn("1. Project A")
            printer.displayLn(
                """
            1. Fix Bug
               ↳ Description: Resolve login issue
               ↳ Start: 2025-05-01, End: 2025-05-02
               ↳ Assigned to: Alice
               ↳ State ID: d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1b
        """.trimIndent()
            )
        }
    }

    @ParameterizedTest
    @CsvSource("2,1", "    ,1", "null,1", nullValues = ["null"])
    fun `should prompt again if invalid project number is entered`(
        firstAttemptEnterNumber: Int?,
        secondAttemptEnterIndex: Int
    ) = runTest {
        // Given
        val project =
            createProject(
                UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"),
                name = "Project A",
                states = listOf(createState(id = UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1b")))
            )
        coEvery { getAllProjectsUseCase() } returns listOf(project)
        coEvery { inputReader.readInt() } returnsMany listOf(firstAttemptEnterNumber, secondAttemptEnterIndex)
        coEvery { getTasksByProjectIdUseCase(project.id) } returns emptyList()
        // When
        presenter.launchUi()
        // Then
        verify { printer.displayLn("\nPlease enter a valid number between 1 and 1.") }
        verify { printer.displayLn("\nNo tasks found in 'Project A'.") }
    }
}
