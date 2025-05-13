package presentation.taskmanagement

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import logic.audit.CreateAuditUseCase
import logic.project.GetAllProjectsUseCase
import logic.task.EditTaskUseCase
import logic.task.GetTasksByProjectIdUseCase
import logic.user.GetLoggedUserUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.io.Printer
import presentation.presentation.utils.PromptService
import presentation.taskmanagement.TestData.fakeDate
import presentation.taskmanagement.TestData.fakeDate2
import presentation.taskmanagement.TestData.fakeProject
import presentation.taskmanagement.TestData.fakeTask
import presentation.taskmanagement.TestData.fakeUser
import presentation.taskmanagement.TestData.testState

class EditTaskUITest {

    private lateinit var printer: Printer
    private lateinit var promptService: PromptService
    private lateinit var getAllProjectsUseCase: GetAllProjectsUseCase
    private lateinit var getTasksByProjectIdUseCase: GetTasksByProjectIdUseCase
    private lateinit var editTaskUseCase: EditTaskUseCase
    private lateinit var createAuditUseCase: CreateAuditUseCase
    private lateinit var getLoggedUserUseCase: GetLoggedUserUseCase
    private lateinit var presenter: EditTaskUI

    @BeforeEach
    fun setup() {
        printer = mockk(relaxed = true)
        promptService = mockk(relaxed = true)
        getAllProjectsUseCase = mockk()
        getTasksByProjectIdUseCase = mockk()
        editTaskUseCase = mockk(relaxed = true)
        createAuditUseCase = mockk(relaxed = true)
        getLoggedUserUseCase = mockk()
        every { getLoggedUserUseCase() } returns fakeUser

        presenter = EditTaskUI(
            printer,
            getLoggedUserUseCase,
            getAllProjectsUseCase,
            getTasksByProjectIdUseCase,
            editTaskUseCase,
            promptService,
            createAuditUseCase
        )
    }

    @Test
    fun `should update title, description, dates, and state`() = runTest {
        //Given
        coEvery { getAllProjectsUseCase() } returns listOf(fakeProject)
        coEvery { getTasksByProjectIdUseCase(fakeProject.projectId) } returns listOf(fakeTask)
        every { promptService.promptSelectionIndex(any(), any()) } returnsMany listOf(0, 0, 0)
        every { promptService.promptString(any(),any()) } returnsMany listOf(
            "NewTitle",
            "NewDesc"
        )
        every { promptService.promptDate(any(), any()) } returnsMany listOf(fakeDate, fakeDate2)

        //When
        presenter.launchUi()

        val expected = fakeTask.copy(
            title = "NewTitle",
            description = "NewDesc",
            startDate = fakeDate,
            endDate = fakeDate2,
            taskStateId = testState.stateId
        )

        //Then
        coVerify { editTaskUseCase(expected) }
        verify { printer.displayLn("\nTask updated successfully.") }
    }

    @Test
    fun `should display error when loading projects fails`() = runTest {
        //Given
        coEvery { getAllProjectsUseCase() } throws RuntimeException("No projects Found")

        //When
        presenter.launchUi()

        //Then
        verify { printer.displayLn(match { it.toString().contains("No projects Found") }) }
    }

    @Test
    fun `should show error message while updating task when the input is not valid`() = runTest {
        //Given
        coEvery { getAllProjectsUseCase() } returns listOf(fakeProject)
        coEvery { getTasksByProjectIdUseCase(fakeProject.projectId) } returns listOf(fakeTask)
        every { promptService.promptSelectionIndex(any(), any()) } returnsMany listOf(0, 0, 0)
        every { promptService.promptNonEmptyString(any()) } returnsMany listOf(
            "New Title",
            "New Desc"
        )
        every { promptService.promptDate(any()) } returnsMany listOf(fakeDate, fakeDate)
        coEvery { editTaskUseCase(any()) } throws RuntimeException("Failed to update task")

        //When
        presenter.launchUi()

        //Then
        verify { printer.displayLn(match { it.toString().contains("Failed to update task") }) }
    }
    
    @Test
    fun `should keep existing title and description if user inputs are empty`() = runTest {
        //Given
        coEvery { getAllProjectsUseCase() } returns listOf(fakeProject)
        coEvery { getTasksByProjectIdUseCase(fakeProject.projectId) } returns listOf(fakeTask)
        every { promptService.promptSelectionIndex(any(), any()) } returnsMany listOf(0, 0, 0)
        every { promptService.promptString(any(), any()) } returnsMany listOf(fakeTask.title, fakeTask.description)
        every { promptService.promptDate(any(), any()) } returnsMany listOf(fakeDate, fakeDate)

        //When
        presenter.launchUi()

        //Then
        coVerify { editTaskUseCase(fakeTask.copy(startDate = fakeDate, endDate = fakeDate)) }
    }



    @Test
    fun `should display error when no tasks exist for selected project`() = runTest {
        //Given
        coEvery { getAllProjectsUseCase() } returns listOf(fakeProject)
        coEvery { getTasksByProjectIdUseCase(fakeProject.projectId) } returns emptyList()
        every { promptService.promptSelectionIndex(any(), any()) } returns 0

        //When
        presenter.launchUi()

        //Then
        verify { printer.displayLn("\nNo tasks found in this project.") }
    }

    @Test
    fun `should display error if no projects are returned`() = runTest {
        //Given
        coEvery { getAllProjectsUseCase() } returns emptyList()

        //When
        presenter.launchUi()

        //Then
        verify { printer.displayLn("\nNo projects available.") }
    }
}

