package presentation.task_management

import helper.createProject
import helper.createTask
import io.mockk.*
import kotlinx.coroutines.test.runTest
import logic.audit.CreateAuditUseCase
import logic.exceptions.NoProjectsFoundException
import logic.exceptions.NoTasksFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import logic.project.GetAllProjectsUseCase
import logic.task.DeleteTaskByIdUseCase
import logic.task.GetTasksByProjectIdUseCase
import logic.user.GetLoggedUserUseCase
import presentation.ui_io.InputReader
import presentation.ui_io.Printer
import java.util.*

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
    fun `should delete task successfully when user input is valid`() = runTest{
        // Given
        val uuid=UUID.randomUUID()
        val project = createProject(id = UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a").toString())
        val task = createTask(id = uuid)

        coEvery { getAllProjectsUseCase() } returns listOf(project)
        coEvery { reader.readInt() } returns 1
        coEvery { getTasksByProjectIdUseCase(project.id.toString()) } returns listOf(task)
        coEvery { reader.readInt() } returns 1
        coEvery { deleteTaskByIdUseCase("1") }just runs
        // When
        presenter.launchUi()
        // Then
        coVerify { deleteTaskByIdUseCase(task.id.toString()) }
        coVerify { printer.displayLn(match { it.toString().contains("deleted successfully") }) }
    }

    @Test
    fun `should display error message when task successfully when user input is valid`() = runTest{
        // Given
        val uuid=UUID.randomUUID()
        val project = createProject(id = UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a").toString())
        val task = createTask(id = uuid)

        coEvery { getAllProjectsUseCase() } returns listOf(project)
        coEvery { reader.readInt() } returns 1
        coEvery { getTasksByProjectIdUseCase(project.id.toString()) } returns listOf(task)
        coEvery { reader.readInt() } returns 1
        coEvery { deleteTaskByIdUseCase("1") } just runs
        // When
        presenter.launchUi()
        // Then
        coVerify { deleteTaskByIdUseCase(task.id.toString()) }
        coVerify { printer.displayLn(match { it.toString().contains("deleted successfully") }) }
    }

    @Test
    fun `should display error when loading projects fails`() = runTest{
        // Given
        coEvery { getAllProjectsUseCase() } throws NoProjectsFoundException()
        // When
        presenter.launchUi()
        // Then
        coVerify { printer.displayLn(match { it.toString().contains("Error loading projects") }) }
    }

    @Test
    fun `should display warning when no projects are available`() = runTest{
        // Given
        coEvery { getAllProjectsUseCase() } returns emptyList()
        // When
        presenter.launchUi()
        // Then
        coVerify { printer.displayLn("No projects available.") }
    }

    @Test
    fun `should display error when loading tasks fails`() = runTest{
        //Given
        val project = createProject()
        coEvery { getAllProjectsUseCase() } returns listOf(project)
        coEvery { reader.readInt() } returns 1
        coEvery { getTasksByProjectIdUseCase("1") } throws NoTasksFoundException()
        // When
        presenter.launchUi()
        // Then
        coVerify { printer.displayLn(match { it.toString().contains("No tasks found") }) }
    }

    @Test
    fun `should display warning when no tasks are found in project`() = runTest{
        // Given
        val project = createProject()
        coEvery { getAllProjectsUseCase() } returns listOf(project)
        coEvery { reader.readInt() } returns 1
        coEvery { getTasksByProjectIdUseCase("1") } returns emptyList()
        // When
        presenter.launchUi()
        // Then
        coVerify { printer.displayLn("No tasks found in this project.") }
    }

    @Test
    fun `should display error when deletion fails`() = runTest{
        // Given
        val project = createProject()
        val task = createTask()

        coEvery { getAllProjectsUseCase() } returns listOf(project)
        coEvery { reader.readInt() } returns 1 andThen 1
        coEvery { getTasksByProjectIdUseCase(any()) } returns listOf(task)
        coEvery { deleteTaskByIdUseCase(any()) } throws Exception("There are error when deleting")
        // When
        presenter.launchUi()
        // Then
        coVerify {
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
    ) = runTest{
        // Given
        val project = createProject()
        val task = createTask()

        coEvery { getAllProjectsUseCase() } returns listOf(project)
        coEvery { getTasksByProjectIdUseCase("1") } returns listOf(task)
        coEvery { reader.readInt() } returns firstAttemptIndexEnter andThen secondAttemptIndexEnter andThen 1
        // When
        presenter.launchUi()
        // Then
        coVerify { printer.displayLn("Please enter a number between 1 and 1.") }
    }

    @Test
    fun `should prompt again when invalid task index is entered`() = runTest{
        // Given
        val task = createTask()
        val project = createProject()

        coEvery { getAllProjectsUseCase() } returns listOf(project)
        coEvery { getTasksByProjectIdUseCase(any()) } returns listOf(task)
        coEvery { reader.readInt() } returns 1 andThen 2 andThen 1
        // When
        presenter.launchUi()
        // Then
        coVerify { printer.displayLn("Please enter a number between 1 and 1.") }
    }

    @Test
    fun `should show error message when get tasks by project id failed`() = runTest{
        // Given
        val project = createProject()
        coEvery { reader.readInt() } returns 1 andThen 1
        coEvery { getAllProjectsUseCase() } returns listOf(project)
        coEvery { getTasksByProjectIdUseCase(any()) } throws NoTasksFoundException()
        // When
        presenter.launchUi()
        // Then
        coVerify { printer.displayLn(match { it.toString().contains("No tasks found") }) }
    }
}
