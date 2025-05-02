package presentation.task_management

import helper.createProject
import helper.createTask
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import squad.abudhabi.logic.exceptions.NoProjectsFoundException
import squad.abudhabi.logic.exceptions.NoTasksFoundException
import squad.abudhabi.logic.project.GetAllProjectsUseCase
import squad.abudhabi.logic.task.EditTaskUseCase
import squad.abudhabi.logic.task.GetTasksByProjectIdUseCase
import squad.abudhabi.presentation.ui_io.InputReader
import squad.abudhabi.presentation.ui_io.Printer

class EditTaskPresenterUITest {

    private val printer = mockk<Printer>(relaxed = true)
    private val inputReader = mockk<InputReader>(relaxed = true)
    private val getAllProjectsUseCase = mockk<GetAllProjectsUseCase>()
    private val getTasksByProjectIdUseCase = mockk<GetTasksByProjectIdUseCase>()
    private val editTaskUseCase = mockk<EditTaskUseCase>(relaxed = true)

    private lateinit var presenter: EditTaskPresenterUI

    @BeforeEach
    fun setup() {
        presenter = EditTaskPresenterUI(
            printer,
            inputReader,
            getAllProjectsUseCase,
            getTasksByProjectIdUseCase,
            editTaskUseCase
        )
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
        val project = createProject("1", "Project A")
        val task = createTask("t1")

        every { getAllProjectsUseCase() } returns listOf(project)
        every { inputReader.readInt() } returnsMany listOf(1, 1)
        every { getTasksByProjectIdUseCase("1") } returns listOf(task)
        every { inputReader.readString() } returnsMany listOf("New Title", "New Desc")

        presenter.launchUi()

        verify { editTaskUseCase(task.copy(title = "New Title", description = "New Desc")) }
        verify { printer.display("Task updated successfully.") }
    }

    @Test
    fun `should show error message while updating task when the input is not valid`() {
        val project = createProject("1", "Project A")
        val task = createTask("t1")

        every { getAllProjectsUseCase() } returns listOf(project)
        every { inputReader.readInt() } returnsMany listOf(1, 1)
        every { getTasksByProjectIdUseCase("1") } returns listOf(task)
        every { inputReader.readString() } returnsMany listOf("New Title", "New Desc")
        every { editTaskUseCase(any()) } throws RuntimeException("Failed to update task")

        presenter.launchUi()

        verify { printer.display(match{it.toString().contains("Failed to update task")}) }
    }

    @Test
    fun `should re-prompt when user enters invalid task selection`() {
        val project = createProject("1", "Project A")
        val task = createTask("t1")

        every { getAllProjectsUseCase() } returns listOf(project)
        every { inputReader.readInt() } returnsMany listOf(1, 0, 1) // project=1, invalid task=0, then valid task=1
        every { getTasksByProjectIdUseCase("1") } returns listOf(task)
        every { inputReader.readString() } returnsMany listOf("Updated Title", "Updated Desc")

        presenter.launchUi()

        verify { printer.display("Please enter a valid number between 1 and 1.") }
    }

    @Test
    fun `should keep existing title and description if user inputs are empty`() {
        val project = createProject("1", "Project A")
        val task = createTask("t1", title = "Old Title", description = "Old Desc")

        every { getAllProjectsUseCase() } returns listOf(project)
        every { inputReader.readInt() } returnsMany listOf(1, 1)
        every { getTasksByProjectIdUseCase("1") } returns listOf(task)
        every { inputReader.readString() } returnsMany listOf("", "")

        presenter.launchUi()

        verify { editTaskUseCase(task.copy(title = "Old Title", description = "Old Desc")) }
    }

    @Test
    fun `should keep existing title and description if user inputs are null`() {
        val project = createProject("1", "Project A")
        val task = createTask("t1", title = "Old Title", description = "Old Desc")

        every { getAllProjectsUseCase() } returns listOf(project)
        every { inputReader.readInt() } returnsMany listOf(1, 1)
        every { getTasksByProjectIdUseCase("1") } returns listOf(task)
        every { inputReader.readString() } returnsMany listOf(null, null)

        presenter.launchUi()

        verify { editTaskUseCase(task.copy(title = "Old Title", description = "Old Desc")) }
    }

    @Test
    fun `should re-prompt if readInt returns null`() {
        val project = createProject("1", "Project A")
        val task = createTask("t1")

        every { getAllProjectsUseCase() } returns listOf(project)
        every { inputReader.readInt() } returnsMany listOf(null, 1, 1)
        every { getTasksByProjectIdUseCase("1") } returns listOf(task)
        every { inputReader.readString() } returnsMany listOf("New", "Updated")

        presenter.launchUi()

        verify { printer.display("Please enter a valid number between 1 and 1.") }
    }



}

