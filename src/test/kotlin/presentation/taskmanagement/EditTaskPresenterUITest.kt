package presentation.taskmanagement

import helper.createProject
import helper.createState
import helper.createTask
import io.mockk.*
import kotlinx.coroutines.test.runTest
import logic.audit.CreateAuditUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import logic.exceptions.NoProjectsFoundException
import logic.exceptions.NoTasksFoundException
import logic.project.GetAllProjectsUseCase
import logic.task.EditTaskUseCase
import logic.task.GetTasksByProjectIdUseCase
import logic.validation.DateParser
import presentation.io.InputReader
import presentation.io.Printer
import logic.user.GetLoggedUserUseCase
import java.time.LocalDate
import java.util.*

class EditTaskPresenterUITest {

    private val printer = mockk<Printer>(relaxed = true)
    private val inputReader = mockk<InputReader>(relaxed = true)
    private val getAllProjectsUseCase = mockk<GetAllProjectsUseCase>()
    private val getTasksByProjectIdUseCase = mockk<GetTasksByProjectIdUseCase>()
    private val editTaskUseCase = mockk<EditTaskUseCase>(relaxed = true)
    private val dateParser: DateParser = mockk(relaxed = true)
    private lateinit var createAuditUseCase: CreateAuditUseCase
    private lateinit var getLoggedUserUseCase: GetLoggedUserUseCase

    private lateinit var presenter: EditTaskPresenterUI

    @BeforeEach
    fun setup() {
        createAuditUseCase = mockk(relaxed = true)
        getLoggedUserUseCase = mockk(relaxed = true)

        presenter = EditTaskPresenterUI(
            printer,
            getLoggedUserUseCase,
            inputReader,
            getAllProjectsUseCase,
            getTasksByProjectIdUseCase,
            editTaskUseCase,
            dateParser,
            createAuditUseCase
        )
    }

    @Test
    fun `should update title, description, dates, and state`() = runTest{
        // Given
        val uuid=UUID.randomUUID()
        val state1 = createState(UUID.fromString("s1"), "Open")
        val state2 = createState(UUID.fromString("s2"), "Closed")
        val project = createProject(id = UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), name = "Project A", states = listOf(state1, state2))
        val task = createTask(
            id=uuid,
            projectId = project.id,
            stateId = UUID.fromString("s1"),
            title = "Old",
            description = "OldDesc",
            startDate = LocalDate.of(2025, 1, 1),
            endDate = LocalDate.of(2025, 1, 2)
        )

        coEvery { getAllProjectsUseCase() } returns listOf(project)
        coEvery { inputReader.readInt() } returnsMany listOf(
            1,
            1,
            1,
        )
        coEvery { getTasksByProjectIdUseCase(project.id) } returns listOf(task)
        // Strings: newTitle, newDesc, newStart, newEnd
        coEvery { inputReader.readString() } returnsMany listOf(
            "NewTitle", "NewDesc", "2025-05-05", "2025-05-10"
        )
        coEvery { dateParser.parseDateFromString("2025-05-05") } returns LocalDate.of(2025, 5, 5)
        coEvery { dateParser.parseDateFromString("2025-05-10") } returns LocalDate.of(2025, 5, 10)

        // When
        presenter.launchUi()

        // Then
        val expected = task.copy(
            title = "NewTitle",
            description = "NewDesc",
            startDate = LocalDate.of(2025, 5, 5),
            endDate = LocalDate.of(2025, 5, 10),
            stateId = UUID.fromString("s1") // first state selected
        )
        coVerify { editTaskUseCase(expected) }
        coVerify { printer.displayLn("✅ Task updated successfully.") }
    }

    @Test
    fun `should displayLn list of tasks when there are projects`() = runTest{
        val project = createProject(name = "Project A")
        coEvery { getAllProjectsUseCase() } returns listOf(project)
        coEvery { inputReader.readInt() } returns 1
        presenter.launchUi()

        coVerify { printer.displayLn(match { it.toString().contains(project.projectName) }) }
    }

    @Test
    fun `should display message when no projects found`() = runTest{
        coEvery { getAllProjectsUseCase() } returns emptyList()

        presenter.launchUi()

        coVerify { printer.displayLn("No projects available.") }
    }

    @Test
    fun `should display error when loading projects fails`() = runTest{
        coEvery { getAllProjectsUseCase() } throws NoProjectsFoundException()

        presenter.launchUi()

        coVerify { printer.displayLn(match { it.toString().contains("No projects Found") }) }
    }

    @Test
    fun `should display message when no tasks in project`() = runTest{
        val project = createProject()
        coEvery { getAllProjectsUseCase() } returns listOf(project)
        coEvery { inputReader.readInt() } returns 1
        coEvery { getTasksByProjectIdUseCase(any()) } returns emptyList()

        presenter.launchUi()

        coVerify { printer.displayLn("No tasks found in this project.") }
    }

    @Test
    fun `should re-prompt when user enters invalid project selection`() = runTest{
        val project = createProject(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "Project A")
        coEvery { getAllProjectsUseCase() } returns listOf(project)

        coEvery { inputReader.readInt() } returnsMany listOf(0, 1)
        coEvery { getTasksByProjectIdUseCase(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a")) } returns emptyList()

        presenter.launchUi()

        coVerify { printer.displayLn("Please enter a valid number between 1 and 1.") }
    }

    @Test
    fun `should display error when loading tasks fails`() = runTest{
        val project = createProject(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "Test Project")
        coEvery { getAllProjectsUseCase() } returns listOf(project)
        coEvery { inputReader.readInt() } returns 1
        coEvery { getTasksByProjectIdUseCase(project.id) } throws NoTasksFoundException()

        presenter.launchUi()

        coVerify { printer.displayLn(match { it.toString().contains("No tasks found") }) }
    }

    @Test
    fun `should successfully update task when the input is valid`() = runTest{
        val uuid=UUID.randomUUID()
        val state = createState(UUID.fromString("s1"), "Open")
        val project = createProject(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "Project A", states = listOf(state))
        val task = createTask(uuid, projectId = UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), stateId = state.id)

        coEvery { getAllProjectsUseCase() } returns listOf(project)
        coEvery { inputReader.readInt() } returnsMany listOf(1, 1, 1)
        coEvery { getTasksByProjectIdUseCase(project.id) } returns listOf(task)
        coEvery { inputReader.readString() } returnsMany listOf("New Title", "New Desc", "", "")

        presenter.launchUi()

        coVerify {
            editTaskUseCase(
                task.copy(title = "New Title", description = "New Desc", stateId = state.id)
            )
        }
        coVerify { printer.displayLn("✅ Task updated successfully.") }
    }


    @Test
    fun `should show error message while updating task when the input is not valid`() = runTest{
        val uuid=UUID.randomUUID()
        val state = createState(UUID.fromString("s1"), "Open")
        val project = createProject(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "Project A", states = listOf(state))
        val task = createTask(uuid, projectId = UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), stateId = state.id)

        coEvery { getAllProjectsUseCase() } returns listOf(project)
        coEvery { inputReader.readInt() } returnsMany listOf(1, 1, 1)
        coEvery { getTasksByProjectIdUseCase(project.id) } returns listOf(task)
        coEvery { inputReader.readString() } returnsMany listOf("New Title", "New Desc", "", "")
        coEvery { editTaskUseCase(any()) } throws RuntimeException("Failed to update task")

        presenter.launchUi()

        coVerify { printer.displayLn(match { it.toString().contains("Failed to update task") }) }
    }

    @Test
    fun `should re-prompt when user enters invalid task selection`() = runTest{
        val uuid=UUID.randomUUID()
        val state = createState(UUID.fromString("s1"), "Open")
        val project = createProject(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "Project A", states = listOf(state))
        val task = createTask(uuid, projectId = UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), stateId = state.id)

        coEvery { getAllProjectsUseCase() } returns listOf(project)
        coEvery { inputReader.readInt() } returnsMany listOf(1, 0, 1, 1)
        coEvery { getTasksByProjectIdUseCase(project.id) } returns listOf(task)
        coEvery { inputReader.readString() } returnsMany listOf(
            "Updated Title",
            "Updated Desc",
            "",
            ""
        )
        presenter.launchUi()

        coVerify { printer.displayLn("Please enter a valid number between 1 and 1.") }
    }

    @Test
    fun `should keep existing title and description if user inputs are empty`() = runTest{
        val uuid=UUID.randomUUID()
        val state = createState(UUID.fromString("s1"), "Open")
        val project = createProject(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "Project A", states = listOf(state))
        val task = createTask(
            id=uuid,
            title = "Old Title",
            description = "Old Desc",
            projectId = UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"),
            stateId = state.id
        )

        coEvery { getAllProjectsUseCase() } returns listOf(project)
        coEvery { inputReader.readInt() } returnsMany listOf(1, 1, 1)
        coEvery { getTasksByProjectIdUseCase(project.id) } returns listOf(task)
        coEvery { inputReader.readString() } returnsMany listOf("", "", "", "")

        presenter.launchUi()

        coVerify { editTaskUseCase(task) }
    }

    @Test
    fun `should keep existing title and description if user inputs are null`() = runTest{
        val uuid=UUID.randomUUID()
        val state = createState(UUID.fromString("s1"), "Open")
        val project = createProject(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "Project A", states = listOf(state))
        val task = createTask(
            id=uuid,
            title = "Old Title",
            description = "Old Desc",
            projectId = UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"),
            stateId = state.id
        )

        coEvery { getAllProjectsUseCase() } returns listOf(project)
        coEvery { inputReader.readInt() } returnsMany listOf(1, 1, 1)
        coEvery { getTasksByProjectIdUseCase(project.id) } returns listOf(task)
        coEvery { inputReader.readString() } returnsMany listOf(null, null, "", "")

        presenter.launchUi()

        coVerify { editTaskUseCase(task) }
    }

    @Test
    fun `should re-prompt if readInt returns null`() = runTest{
        val uuid=UUID.randomUUID()
        val state = createState(UUID.fromString("s1"), "Open")
        val project = createProject(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "Project A", states = listOf(state))
        val task = createTask(uuid, projectId = UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), stateId = state.id)

        coEvery { getAllProjectsUseCase() } returns listOf(project)
        coEvery { inputReader.readInt() } returnsMany listOf(null, 1, 1, 1)
        coEvery { getTasksByProjectIdUseCase(project.id) } returns listOf(task)
        coEvery { inputReader.readString() } returnsMany listOf("New", "Updated", "", "")

        presenter.launchUi()

        coVerify { printer.displayLn("Please enter a valid number between 1 and 1.") }
    }


    @Test
    fun `should show date format error message when user enter invalid date format`() = runTest{
        // Given
        val uuid=UUID.randomUUID()
        val state1 = createState(UUID.fromString("s1"), "Open")
        val state2 = createState(UUID.fromString("s2"), "Closed")
        val project = createProject(id = UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), name = "Project A", states = listOf(state1, state2))
        val task = createTask(
            id=uuid,
            projectId = UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"),
            stateId = UUID.fromString("s1"),
            title = "Old",
            description = "OldDesc",
            startDate = LocalDate.of(2025, 1, 1),
            endDate = LocalDate.of(2025, 1, 2)
        )

        coEvery { getAllProjectsUseCase() } returns listOf(project)
        coEvery { inputReader.readInt() } returnsMany listOf(
            1,
            1,
            1,
        )
        coEvery { getTasksByProjectIdUseCase(project.id) } returns listOf(task)
        // Strings: newTitle, newDesc, newStart, newEnd
        coEvery { inputReader.readString() } returnsMany listOf(
            "NewTitle", "NewDesc", "Invalid date", "2025-05-10"
        )
        coEvery { dateParser.parseDateFromString("Invalid date") } throws Exception("Invalid format")
        coEvery { dateParser.parseDateFromString("2025-05-10") } returns LocalDate.of(2025, 5, 10)

        // When
        presenter.launchUi()

        // Then
        val expected = task.copy(
            title = "NewTitle",
            description = "NewDesc",
            startDate = LocalDate.of(2025, 5, 5),
            endDate = LocalDate.of(2025, 5, 10),
            stateId = UUID.fromString("s1") // first state selected
        )
        coVerify {
            printer.displayLn(match {
                it.toString().contains("Invalid date format. Keeping current value.")
            })
        }
    }

}

