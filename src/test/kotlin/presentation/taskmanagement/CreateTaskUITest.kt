package presentation.taskmanagement

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import logic.audit.CreateAuditUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import logic.project.GetAllProjectsUseCase
import logic.task.CreateTaskUseCase
import presentation.io.Printer
import logic.user.GetLoggedUserUseCase
import presentation.utils.PromptService
import presentation.taskmanagement.TestData.fakeDate
import presentation.taskmanagement.TestData.fakeProject
import presentation.taskmanagement.TestData.fakeUser

class CreateTaskUITest {
    private lateinit var promptService: PromptService
    private lateinit var printer: Printer
    private lateinit var createTaskUseCase: CreateTaskUseCase
    private lateinit var getAllProjectsUseCase: GetAllProjectsUseCase
    private lateinit var createAuditUseCase: CreateAuditUseCase
    private lateinit var getLoggedUserUseCase: GetLoggedUserUseCase
    private lateinit var createTaskUI: CreateTaskUI

    @BeforeEach
    fun setup() {
        promptService = mockk(relaxed = true)
        printer = mockk(relaxed = true)
        createTaskUseCase = mockk(relaxed = true)
        getAllProjectsUseCase = mockk(relaxed = true)
        createAuditUseCase = mockk(relaxed = true)
        getLoggedUserUseCase = mockk()

        every { getLoggedUserUseCase() } returns fakeUser

        createTaskUI = CreateTaskUI(
            getLoggedUserUseCase,
            printer,
            getAllProjectsUseCase,
            createTaskUseCase,
            promptService,
            createAuditUseCase
        )
    }

    @Test
    fun `should create a task when user input is valid`() = runTest {
        //Given
        every { promptService.promptNonEmptyString(any()) } returnsMany listOf("Task Title", "Task Desc")
        every { promptService.promptDate(any()) } returnsMany listOf(fakeDate, fakeDate)
        every { promptService.promptSelectionIndex(any(), any()) } returnsMany listOf(0, 0)
        coEvery { getAllProjectsUseCase() } returns listOf(fakeProject)

        //When
        createTaskUI.launchUi()

        //Then
        coVerify { createTaskUseCase(match { it.title == "Task Title" }) }
        verify { printer.displayLn("\nTask created successfully.") }
    }

    @Test
    fun `should display error message when get all projects use case throw error`() = runTest {
        //Given
        coEvery { getAllProjectsUseCase() } throws RuntimeException("DB Error")
        every { promptService.promptNonEmptyString(any()) } returnsMany listOf("Title", "Desc")
        every { promptService.promptDate(any()) } returnsMany listOf(fakeDate, fakeDate)

        //When
        createTaskUI.launchUi()

        //Then
        verify { printer.displayLn(match { it.toString().contains("Error loading projects") }) }
    }

    @Test
    fun `should show message when no projects available`() = runTest {
        //Given
        coEvery { getAllProjectsUseCase() } returns emptyList()
        every { promptService.promptNonEmptyString(any()) } returnsMany listOf("Title", "Desc")
        every { promptService.promptDate(any()) } returnsMany listOf(fakeDate, fakeDate)

        //When
        createTaskUI.launchUi()

        //Then
        verify { printer.displayLn("\nNo projects available.") }
    }

    @Test
    fun `should display error message when failed to create task`() = runTest {
        //Given
        every { promptService.promptNonEmptyString(any()) } returnsMany listOf("Title", "Desc")
        every { promptService.promptDate(any()) } returnsMany listOf(fakeDate, fakeDate)
        every { promptService.promptSelectionIndex(any(), any()) } returnsMany listOf(0, 0)
        coEvery { getAllProjectsUseCase() } returns listOf(fakeProject)
        coEvery { createTaskUseCase(any()) } throws RuntimeException("Failed")

        //When
        createTaskUI.launchUi()

        //Then
        verify { printer.displayLn(match { it.toString().contains("Failed to create task")}) }
    }

    @Test
    fun `should print project and task states correctly`() = runTest {
        //Given
        every { promptService.promptNonEmptyString(any()) } returnsMany listOf("Title", "Desc")
        every { promptService.promptDate(any()) } returnsMany listOf(fakeDate, fakeDate)
        every { promptService.promptSelectionIndex(any(), any()) } returnsMany listOf(0, 0)
        coEvery { getAllProjectsUseCase() } returns listOf(fakeProject)

        //When
        createTaskUI.launchUi()

        //Then
        verify { printer.displayLn("\nAvailable projects:") }
        verify { printer.displayLn("1. Project A") }
        verify { printer.displayLn("\nAvailable taskStates:") }
        verify { printer.displayLn("1. To Do") }
    }
}