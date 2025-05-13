package presentation.project

import helper.createProject
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import logic.audit.CreateAuditUseCase
import logic.model.TaskState
import logic.project.EditStateOfProjectUseCase
import logic.project.GetAllProjectsUseCase
import org.junit.jupiter.api.Test
import presentation.io.Printer
import presentation.utils.PromptService
import kotlin.test.BeforeTest

class EditTaskStateOfProjectUITest {
    private val editStateOfProjectUseCase: EditStateOfProjectUseCase = mockk(relaxed = true)
    private val getAllProjectsUseCase: GetAllProjectsUseCase = mockk(relaxed = true)
    private val promptService: PromptService = mockk(relaxed = true)
    private val printer: Printer = mockk(relaxed = true)
    private lateinit var ui: EditStateOfProjectUI
    private val createAuditUseCase: CreateAuditUseCase = mockk(relaxed = true)

    @BeforeTest
    fun setup() {
        ui = EditStateOfProjectUI(
            editStateOfProjectUseCase = editStateOfProjectUseCase,
            getAllProjectsUseCase = getAllProjectsUseCase,
            promptService = promptService,
            printer = printer,
            getLoggedUserUseCase = mockk(relaxed = true),
            createAuditUseCase = createAuditUseCase
        )
    }

    @Test
    fun `launchUI should edit successfully when nothing went wrong`() = runTest{
        //Given
        val project = createProject(taskStates = listOf(TaskState(stateName = "state1")))
        val projects = listOf(project)
        coEvery { getAllProjectsUseCase() } returns projects
        every { promptService.promptSelectionIndex(any(),any()) } returns 0 andThen 0
        every { promptService.promptNonEmptyString(any()) } returns "newName"
        //When
        ui.launchUi()
        //Then
        verify { project.taskStates.forEachIndexed { index, state ->
            printer.displayLn(match { it.toString().contains(state.stateName) })
        } }
        coVerify { editStateOfProjectUseCase(any(),any()) }
        verify { printer.displayLn(match { it.toString().contains("updated") }) }
    }

    @Test
    fun `launchUI should display Exception message when get all projects throw Exception`() = runTest{
        //Given
        coEvery { getAllProjectsUseCase() } throws Exception()
        //When
        ui.launchUi()
        //Then
        verify { printer.displayLn(match { it.toString().contains("${Exception().message}") }) }
    }

    @Test
    fun `launchUI should display no project message when get all projects return empty list`() = runTest{
        //Given
        coEvery { getAllProjectsUseCase() } returns emptyList()
        //When
        ui.launchUi()
        //Then
        verify { printer.displayLn(match { it.toString().contains("no project") }) }
    }

    @Test
    fun `launchUI should display Exception message editStateOfProjectUseCase when throw Exception`() = runTest{
        //Given
        val project = createProject(taskStates = listOf(TaskState(stateName = "state1")))
        val projects = listOf(project)
        coEvery { getAllProjectsUseCase() } returns projects
        every { promptService.promptSelectionIndex(any(),any()) } returns 0 andThen 0
        every { promptService.promptNonEmptyString(any()) } returns "newName"
        coEvery { editStateOfProjectUseCase(any(),any()) } throws Exception()
        //When
        ui.launchUi()
        //Then
        verify { printer.displayLn(match { it.toString().contains("${Exception().message}") }) }
    }

    @Test
    fun `launchUI should call add audit use case when update project state successfully`() = runTest {
        //Given
        val project = createProject(taskStates = listOf(TaskState(stateName = "state1")))
        val projects = listOf(project)
        coEvery { getAllProjectsUseCase() } returns projects
        every { promptService.promptSelectionIndex(any(), any()) } returns 0 andThen 0
        every { promptService.promptNonEmptyString(any()) } returns "newName"
        //When
        ui.launchUi()
        //Then
        coVerify { createAuditUseCase(any()) }
    }

    @Test
    fun `launchUI should not call add audit use case when update project state fails`() = runTest {
        //Given
        val project = createProject(taskStates = listOf(TaskState(stateName = "state1")))
        val projects = listOf(project)
        coEvery { getAllProjectsUseCase() } returns projects
        every { promptService.promptSelectionIndex(any(), any()) } returns 0 andThen 0
        every { promptService.promptNonEmptyString(any()) } returns "newName"
        coEvery { editStateOfProjectUseCase(any(), any()) } throws Exception()
        //When
        ui.launchUi()
        //Then
        coVerify(exactly = 0) { createAuditUseCase(any()) }
    }
}
