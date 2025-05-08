package presentation.task_management

import helper.createProject
import helper.createTask
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import logic.audit.CreateAuditUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import logic.exceptions.NoProjectsFoundException
import logic.exceptions.NoTasksFoundException
import logic.project.GetAllProjectsUseCase
import logic.task.DeleteTaskByIdUseCase
import logic.task.GetTasksByProjectIdUseCase
import presentation.ui_io.InputReader
import presentation.ui_io.Printer
import logic.user.GetLoggedUserUseCase

class DeleteTaskByIdPresenterUITest {

    private lateinit var printer: Printer
    private lateinit var reader: InputReader
    private lateinit var getAllProjectsUseCase: GetAllProjectsUseCase
    private lateinit var getTasksByProjectIdUseCase: GetTasksByProjectIdUseCase
    private lateinit var deleteTaskByIdUseCase: DeleteTaskByIdUseCase
    private lateinit var presenter: DeleteTaskByIdPresenterUI
    private lateinit var createAuditUseCase: CreateAuditUseCase
    private lateinit var getLoggedUserUseCase : GetLoggedUserUseCase

    @BeforeEach
    fun setUp() {
        printer = mockk(relaxed = true)
        reader = mockk(relaxed = true)
        getAllProjectsUseCase = mockk(relaxed = true)
        getTasksByProjectIdUseCase = mockk(relaxed = true)
        deleteTaskByIdUseCase = mockk(relaxed = true)
        createAuditUseCase = mockk(relaxed = true)
        getLoggedUserUseCase = mockk(relaxed =true)

        presenter = DeleteTaskByIdPresenterUI(
            printer,
            getLoggedUserUseCase,
            reader,
            getAllProjectsUseCase,
            getTasksByProjectIdUseCase,
            deleteTaskByIdUseCase,
            createAuditUseCase
        )
    }

    @Test
    fun `should delete task successfully when user input is valid`() {
        // Given
        val project = createProject(id = "1")
        val task = createTask(id = "1")

        every { getAllProjectsUseCase() } returns listOf(project)
        every { reader.readInt() } returns 1
        every { getTasksByProjectIdUseCase("1") } returns listOf(task)
        every { reader.readInt() } returns 1
        justRun { deleteTaskByIdUseCase("1") }
        // When
        presenter.launchUi()
        // Then
        verify { deleteTaskByIdUseCase(task.id) }
        verify { printer.displayLn(match { it.toString().contains("deleted successfully") }) }
    }

    @Test
    fun `should display error message when task successfully when user input is valid`() {
        // Given
        val project = createProject(id = "1")
        val task = createTask(id = "1")

        every { getAllProjectsUseCase() } returns listOf(project)
        every { reader.readInt() } returns 1
        every { getTasksByProjectIdUseCase("1") } returns listOf(task)
        every { reader.readInt() } returns 1
        justRun { deleteTaskByIdUseCase("1") }
        // When
        presenter.launchUi()
        // Then
        verify { deleteTaskByIdUseCase(task.id) }
        verify { printer.displayLn(match { it.toString().contains("deleted successfully") }) }
    }

    @Test
    fun `should display error when loading projects fails`() {
        // Given
        every { getAllProjectsUseCase() } throws NoProjectsFoundException()
        // When
        presenter.launchUi()
        // Then
        verify { printer.displayLn(match { it.toString().contains("Error loading projects") }) }
    }

    @Test
    fun `should display warning when no projects are available`() {
        // Given
        every { getAllProjectsUseCase() } returns emptyList()
        // When
        presenter.launchUi()
        // Then
        verify { printer.displayLn("No projects available.") }
    }

    @Test
    fun `should display error when loading tasks fails`() {
        //Given
        val project = createProject()
        every { getAllProjectsUseCase() } returns listOf(project)
        every { reader.readInt() } returns 1
        every { getTasksByProjectIdUseCase("1") } throws NoTasksFoundException()
        // When
        presenter.launchUi()
        // Then
        verify { printer.displayLn(match { it.toString().contains("No tasks found") }) }
    }

    @Test
    fun `should display warning when no tasks are found in project`() {
        // Given
        val project = createProject()
        every { getAllProjectsUseCase() } returns listOf(project)
        every { reader.readInt() } returns 1
        every { getTasksByProjectIdUseCase("1") } returns emptyList()
        // When
        presenter.launchUi()
        // Then
        verify { printer.displayLn("No tasks found in this project.") }
    }

    @Test
    fun `should display error when deletion fails`() {
        // Given
        val project = createProject()
        val task = createTask()

        every { getAllProjectsUseCase() } returns listOf(project)
        every { reader.readInt() } returns 1 andThen 1
        every { getTasksByProjectIdUseCase(any()) } returns listOf(task)
        every { deleteTaskByIdUseCase(any()) } throws Exception("There are error when deleting")
        // When
        presenter.launchUi()
        // Then
        verify {
            printer.displayLn(match {
                it.toString().contains("There are error when deleting")
            })
        }
    }

    @ParameterizedTest
    @CsvSource("2,1","null,1" , nullValues = ["null"])
    fun `should prompt again when invalid project index is entered`(
        firstAttemptIndexEnter: Int?,
        secondAttemptIndexEnter: Int
    ) {
        // Given
        val project = createProject()
        val task = createTask()

        every { getAllProjectsUseCase() } returns listOf(project)
        every { getTasksByProjectIdUseCase("1") } returns listOf(task)
        every { reader.readInt() } returns firstAttemptIndexEnter andThen secondAttemptIndexEnter andThen 1
        // When
        presenter.launchUi()
        // Then
        verify { printer.displayLn("Please enter a number between 1 and 1.") }
    }

    @Test
    fun `should prompt again when invalid task index is entered`() {
        // Given
        val task = createTask()
        val project = createProject()

        every { getAllProjectsUseCase() } returns listOf(project)
        every { getTasksByProjectIdUseCase(any()) } returns listOf(task)
        every { reader.readInt() } returns 1 andThen 2 andThen 1
        // When
        presenter.launchUi()
        // Then
        verify { printer.displayLn("Please enter a number between 1 and 1.") }
    }

    @Test
    fun `should show error message when get tasks by project id failed`() {
        // Given
        val project = createProject()
        every { reader.readInt() } returns 1 andThen 1
        every { getAllProjectsUseCase() } returns listOf(project)
        every { getTasksByProjectIdUseCase(any()) } throws NoTasksFoundException()
        // When
        presenter.launchUi()
        // Then
        verify { printer.displayLn(match { it.toString().contains("No tasks found") }) }
    }
}
