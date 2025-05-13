package presentation.taskmanagement

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import logic.audit.CreateAuditUseCase
import logic.exceptions.NoProjectsFoundException
import logic.exceptions.NoTasksFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import logic.project.GetAllProjectsUseCase
import logic.task.DeleteTaskByIdUseCase
import logic.task.GetTasksByProjectIdUseCase
import logic.user.GetLoggedUserUseCase
import presentation.io.Printer
import presentation.presentation.utils.PromptService

class DeleteTaskByIdUITest {

    private lateinit var printer: Printer
    private lateinit var promptService: PromptService
    private lateinit var getAllProjectsUseCase: GetAllProjectsUseCase
    private lateinit var getTasksByProjectIdUseCase: GetTasksByProjectIdUseCase
    private lateinit var deleteTaskByIdUseCase: DeleteTaskByIdUseCase
    private lateinit var createAuditUseCase: CreateAuditUseCase
    private lateinit var getLoggedUserUseCase: GetLoggedUserUseCase
    private lateinit var deleteTaskByIdUI: DeleteTaskByIdUI

    @BeforeEach
    fun setUp() {
        printer = mockk(relaxed = true)
        promptService = mockk(relaxed = true)
        getAllProjectsUseCase = mockk(relaxed = true)
        getTasksByProjectIdUseCase = mockk(relaxed = true)
        deleteTaskByIdUseCase = mockk(relaxed = true)
        createAuditUseCase = mockk(relaxed = true)
        getLoggedUserUseCase = mockk(relaxed = true)

        every { getLoggedUserUseCase() } returns TestData.fakeUser

        deleteTaskByIdUI = DeleteTaskByIdUI(
            printer,
            getLoggedUserUseCase,
            getAllProjectsUseCase,
            getTasksByProjectIdUseCase,
            deleteTaskByIdUseCase,
            createAuditUseCase,
            promptService
        )
    }

    @Test
    fun `should delete task successfully when user input is valid`() = runTest {
        //Given
        coEvery { getAllProjectsUseCase() } returns listOf(TestData.fakeProject)
        coEvery { getTasksByProjectIdUseCase(TestData.fakeProject.projectId) } returns listOf(TestData.fakeTask)
        every { promptService.promptSelectionIndex(any(), any()) } returnsMany listOf(0, 0)

        //When
        deleteTaskByIdUI.launchUi()

        //Then
        coVerify { deleteTaskByIdUseCase(TestData.fakeTask.taskId) }
        verify { printer.displayLn(match { it.toString().contains("deleted successfully") }) }
    }

    @Test
    fun `should display error when loading projects fails`() = runTest {
        //Given
        coEvery { getAllProjectsUseCase() } throws NoProjectsFoundException()

        //When
        deleteTaskByIdUI.launchUi()

        //Then
        verify { printer.displayLn(match { it.toString().contains("Error loading projects") }) }

    }

    @Test
    fun `should display warning when no projects are available`() = runTest {
        //Given
        coEvery { getAllProjectsUseCase() } returns emptyList()

        //When
        deleteTaskByIdUI.launchUi()

        //Then
        verify { printer.displayLn("\nNo projects available.") }
    }

    @Test
    fun `should display error when loading tasks fails`() = runTest {
        //Given
        coEvery { getAllProjectsUseCase() } returns listOf(TestData.fakeProject)
        every { promptService.promptSelectionIndex(any(), any()) } returns 0
        coEvery { getTasksByProjectIdUseCase(TestData.fakeProject.projectId) } throws NoTasksFoundException()

        //When
        deleteTaskByIdUI.launchUi()

        //Then
        verify { printer.displayLn(match { it.toString().contains("Error loading tasks") }) }
    }

    @Test
    fun `should display warning when no tasks are found in project`() = runTest {
        //Given
        coEvery { getAllProjectsUseCase() } returns listOf(TestData.fakeProject)
        every { promptService.promptSelectionIndex(any(), any()) } returns 0
        coEvery { getTasksByProjectIdUseCase(TestData.fakeProject.projectId) } returns emptyList()

        //When
        deleteTaskByIdUI.launchUi()

        //Then
        verify { printer.displayLn("\nNo tasks found in this project.") }
    }

    @Test
    fun `should display error when deletion fails`() = runTest {
        //Given
        coEvery { getAllProjectsUseCase() } returns listOf(TestData.fakeProject)
        coEvery { getTasksByProjectIdUseCase(TestData.fakeProject.projectId) } returns listOf(TestData.fakeTask)
        every { promptService.promptSelectionIndex(any(), any()) } returnsMany listOf(0, 0)
        coEvery { deleteTaskByIdUseCase(any()) } throws Exception("There are error when deleting")

        //When
        deleteTaskByIdUI.launchUi()

        //Then
        verify { printer.displayLn(match { it.toString().contains("There are error when deleting") }) }
    }

    @Test
    fun `should show error message when get tasks by project id failed`() = runTest {
        //Given
        coEvery { getAllProjectsUseCase() } returns listOf(TestData.fakeProject)
        every { promptService.promptSelectionIndex(any(), any()) } returns 0
        coEvery { getTasksByProjectIdUseCase(TestData.fakeProject.projectId) } throws NoTasksFoundException()

        //When
        deleteTaskByIdUI.launchUi()

        //Then
        verify { printer.displayLn(match { it.toString().contains("No tasks found") }) }
    }
}
