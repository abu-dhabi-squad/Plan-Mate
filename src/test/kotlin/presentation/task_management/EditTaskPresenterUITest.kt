package presentation.task_management

import helper.createProject
import helper.createState
import helper.createTask
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.audit.CreateAuditUseCase
import logic.validation.DateParser
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import squad.abudhabi.logic.exceptions.NoProjectsFoundException
import squad.abudhabi.logic.exceptions.NoTasksFoundException
import squad.abudhabi.logic.project.GetAllProjectsUseCase
import squad.abudhabi.logic.task.EditTaskUseCase
import squad.abudhabi.logic.task.GetTasksByProjectIdUseCase
import squad.abudhabi.logic.user.GetLoggedUserUseCase
import squad.abudhabi.presentation.ui_io.InputReader
import squad.abudhabi.presentation.ui_io.Printer
import java.time.LocalDate

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
    fun `should update title, description, dates, and state`() {
        // Given
        val state1 = createState("s1", "Open")
        val state2 = createState("s2", "Closed")
        val project = createProject(id = "p1", name = "Project A", states = listOf(state1, state2))
        val task = createTask(
            "t1",
            projectId = "p1",
            stateId = "s1",
            title = "Old",
            description = "OldDesc",
            startDate = LocalDate.of(2025, 1, 1),
            endDate = LocalDate.of(2025, 1, 2)
        )

        every { getAllProjectsUseCase() } returns listOf(project)
        every { inputReader.readInt() } returnsMany listOf(
            1,
            1,
            1,
        )
        every { getTasksByProjectIdUseCase("p1") } returns listOf(task)
        // Strings: newTitle, newDesc, newStart, newEnd
        every { inputReader.readString() } returnsMany listOf(
            "NewTitle", "NewDesc", "2025-05-05", "2025-05-10"
        )
        every { dateParser.parseDateFromString("2025-05-05") } returns LocalDate.of(2025, 5, 5)
        every { dateParser.parseDateFromString("2025-05-10") } returns LocalDate.of(2025, 5, 10)

        // When
        presenter.launchUi()

        // Then
        val expected = task.copy(
            title = "NewTitle",
            description = "NewDesc",
            startDate = LocalDate.of(2025, 5, 5),
            endDate = LocalDate.of(2025, 5, 10),
            stateId = "s1" // first state selected
        )
        verify { editTaskUseCase(expected) }
        verify { printer.display("✅ Task updated successfully.") }
    }

    @Test
    fun `should display list of tasks when there are projects`() {
        val project = createProject(name = "Project A")
        every { getAllProjectsUseCase() } returns listOf(project)
        every { inputReader.readInt() } returns 1
        presenter.launchUi()

        verify { printer.display(match { it.toString().contains(project.projectName) }) }
    }

    @Test
    fun `should display message when no projects found`() {
        every { getAllProjectsUseCase() } returns emptyList()

        presenter.launchUi()

        verify { printer.display("No projects available.") }
    }

    @Test
    fun `should display error when loading projects fails`() {
        every { getAllProjectsUseCase() } throws NoProjectsFoundException()

        presenter.launchUi()

        verify { printer.display(match { it.toString().contains("No projects Found") }) }
    }

    @Test
    fun `should display message when no tasks in project`() {
        val project = createProject()
        every { getAllProjectsUseCase() } returns listOf(project)
        every { inputReader.readInt() } returns 1
        every { getTasksByProjectIdUseCase(any()) } returns emptyList()

        presenter.launchUi()

        verify { printer.display("No tasks found in this project.") }
    }

    @Test
    fun `should re-prompt when user enters invalid project selection`() {
        val project = createProject("1", "Project A")
        every { getAllProjectsUseCase() } returns listOf(project)

        every { inputReader.readInt() } returnsMany listOf(0, 1)
        every { getTasksByProjectIdUseCase("1") } returns emptyList()

        presenter.launchUi()

        verify { printer.display("Please enter a valid number between 1 and 1.") }
    }

    @Test
    fun `should display error when loading tasks fails`() {
        val project = createProject("1", "Test Project")
        every { getAllProjectsUseCase() } returns listOf(project)
        every { inputReader.readInt() } returns 1
        every { getTasksByProjectIdUseCase("1") } throws NoTasksFoundException()

        presenter.launchUi()

        verify { printer.display(match { it.toString().contains("No tasks found") }) }
    }

    @Test
    fun `should successfully update task when the input is valid`() {
        val state = createState("s1", "Open")
        val project = createProject("1", "Project A", states = listOf(state))
        val task = createTask("t1", projectId = "1", stateId = state.id)

        every { getAllProjectsUseCase() } returns listOf(project)
        every { inputReader.readInt() } returnsMany listOf(1, 1, 1)
        every { getTasksByProjectIdUseCase("1") } returns listOf(task)
        every { inputReader.readString() } returnsMany listOf("New Title", "New Desc", "", "")

        presenter.launchUi()

        verify {
            editTaskUseCase(
                task.copy(title = "New Title", description = "New Desc", stateId = state.id)
            )
        }
        verify { printer.display("✅ Task updated successfully.") }
    }


    @Test
    fun `should show error message while updating task when the input is not valid`() {
        val state = createState("s1", "Open")
        val project = createProject("1", "Project A", states = listOf(state))
        val task = createTask("t1", projectId = "1", stateId = state.id)

        every { getAllProjectsUseCase() } returns listOf(project)
        every { inputReader.readInt() } returnsMany listOf(1, 1, 1)
        every { getTasksByProjectIdUseCase("1") } returns listOf(task)
        every { inputReader.readString() } returnsMany listOf("New Title", "New Desc", "", "")
        every { editTaskUseCase(any()) } throws RuntimeException("Failed to update task")

        presenter.launchUi()

        verify { printer.display(match { it.toString().contains("Failed to update task") }) }
    }

    @Test
    fun `should re-prompt when user enters invalid task selection`() {
        val state = createState("s1", "Open")
        val project = createProject("1", "Project A", states = listOf(state))
        val task = createTask("t1", projectId = "1", stateId = state.id)

        every { getAllProjectsUseCase() } returns listOf(project)
        every { inputReader.readInt() } returnsMany listOf(1, 0, 1, 1)
        every { getTasksByProjectIdUseCase("1") } returns listOf(task)
        every { inputReader.readString() } returnsMany listOf(
            "Updated Title",
            "Updated Desc",
            "",
            ""
        )

        presenter.launchUi()

        verify { printer.display("Please enter a valid number between 1 and 1.") }
    }


    @Test
    fun `should keep existing title and description if user inputs are empty`() {
        val state = createState("s1", "Open")
        val project = createProject("1", "Project A", states = listOf(state))
        val task = createTask(
            "t1",
            title = "Old Title",
            description = "Old Desc",
            projectId = "1",
            stateId = state.id
        )

        every { getAllProjectsUseCase() } returns listOf(project)
        every { inputReader.readInt() } returnsMany listOf(1, 1, 1)
        every { getTasksByProjectIdUseCase("1") } returns listOf(task)
        every { inputReader.readString() } returnsMany listOf("", "", "", "")

        presenter.launchUi()

        verify { editTaskUseCase(task) }
    }

    @Test
    fun `should keep existing title and description if user inputs are null`() {
        val state = createState("s1", "Open")
        val project = createProject("1", "Project A", states = listOf(state))
        val task = createTask(
            "t1",
            title = "Old Title",
            description = "Old Desc",
            projectId = "1",
            stateId = state.id
        )

        every { getAllProjectsUseCase() } returns listOf(project)
        every { inputReader.readInt() } returnsMany listOf(1, 1, 1)
        every { getTasksByProjectIdUseCase("1") } returns listOf(task)
        every { inputReader.readString() } returnsMany listOf(null, null, "", "")

        presenter.launchUi()

        verify { editTaskUseCase(task) }
    }

    @Test
    fun `should re-prompt if readInt returns null`() {
        val state = createState("s1", "Open")
        val project = createProject("1", "Project A", states = listOf(state))
        val task = createTask("t1", projectId = "1", stateId = state.id)

        every { getAllProjectsUseCase() } returns listOf(project)
        every { inputReader.readInt() } returnsMany listOf(null, 1, 1, 1)
        every { getTasksByProjectIdUseCase("1") } returns listOf(task)
        every { inputReader.readString() } returnsMany listOf("New", "Updated", "", "")

        presenter.launchUi()

        verify { printer.display("Please enter a valid number between 1 and 1.") }
    }


    @Test
    fun `should show date format error message when user enter invalid date format`() {
        // Given
        val state1 = createState("s1", "Open")
        val state2 = createState("s2", "Closed")
        val project = createProject(id = "p1", name = "Project A", states = listOf(state1, state2))
        val task = createTask(
            "t1",
            projectId = "p1",
            stateId = "s1",
            title = "Old",
            description = "OldDesc",
            startDate = LocalDate.of(2025, 1, 1),
            endDate = LocalDate.of(2025, 1, 2)
        )

        every { getAllProjectsUseCase() } returns listOf(project)
        every { inputReader.readInt() } returnsMany listOf(
            1,
            1,
            1,
        )
        every { getTasksByProjectIdUseCase("p1") } returns listOf(task)
        // Strings: newTitle, newDesc, newStart, newEnd
        every { inputReader.readString() } returnsMany listOf(
            "NewTitle", "NewDesc", "Invalid date", "2025-05-10"
        )
        every { dateParser.parseDateFromString("Invalid date") } throws Exception("Invalid format")
        every { dateParser.parseDateFromString("2025-05-10") } returns LocalDate.of(2025, 5, 10)

        // When
        presenter.launchUi()

        // Then
        val expected = task.copy(
            title = "NewTitle",
            description = "NewDesc",
            startDate = LocalDate.of(2025, 5, 5),
            endDate = LocalDate.of(2025, 5, 10),
            stateId = "s1" // first state selected
        )
        verify {
            printer.display(match {
                it.toString().contains("Invalid date format. Keeping current value.")
            })
        }
    }

}

