package presentation.project

import helper.createProject
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import logic.model.TaskState
import logic.project.EditStateOfProjectUseCase
import logic.project.GetAllProjectsUseCase
import org.junit.jupiter.api.Test
import presentation.io.Printer
import presentation.presentation.utils.PromptService
import kotlin.test.BeforeTest

class EditTaskStateOfProjectUITest {
    private val editStateOfProjectUseCase: EditStateOfProjectUseCase = mockk(relaxed = true)
    private val getAllProjectsUseCase: GetAllProjectsUseCase = mockk(relaxed = true)
    private val promptService: PromptService = mockk(relaxed = true)
    private val printer: Printer = mockk(relaxed = true)
    private lateinit var ui: EditStateOfProjectUI

    @BeforeTest
    fun setup() {
        ui = EditStateOfProjectUI(
            editStateOfProjectUseCase = editStateOfProjectUseCase,
            getAllProjectsUseCase = getAllProjectsUseCase,
            promptService = promptService,
            printer = printer
        )
    }

    @Test
    fun `launchUI should edit successfully when nothing went wrong`() = runTest{
        //given
        val project = createProject(taskStates = listOf(TaskState(stateName = "state1")))
        val projects = listOf(project)
        coEvery { getAllProjectsUseCase() } returns projects
        every { promptService.promptSelectionIndex(any(),any()) } returns 0 andThen 0
        every { promptService.promptNonEmptyString(any()) } returns "newName"
        //when
        ui.launchUi()
        //then
        verify { project.taskStates.forEachIndexed { index, state ->
            printer.displayLn(match { it.toString().contains(state.stateName) })
        } }
        coVerify { editStateOfProjectUseCase(any(),any()) }
        verify { printer.displayLn(match { it.toString().contains("updated") }) }
    }

    @Test
    fun `launchUI should display Exception message when get all projects throw Exception`() = runTest{
        //given
        coEvery { getAllProjectsUseCase() } throws Exception()
        //when
        ui.launchUi()
        //then
        verify { printer.displayLn(match { it.toString().contains("${Exception().message}") }) }
    }

    @Test
    fun `launchUI should display no project message when get all projects return empty list`() = runTest{
        //given
        coEvery { getAllProjectsUseCase() } returns emptyList()
        //when
        ui.launchUi()
        //then
        verify { printer.displayLn(match { it.toString().contains("no project") }) }
    }

    @Test
    fun `launchUI should display Exception message editStateOfProjectUseCase when throw Exception`() = runTest{
        //given
        val project = createProject(taskStates = listOf(TaskState(stateName = "state1")))
        val projects = listOf(project)
        coEvery { getAllProjectsUseCase() } returns projects
        every { promptService.promptSelectionIndex(any(),any()) } returns 0 andThen 0
        every { promptService.promptNonEmptyString(any()) } returns "newName"
        coEvery { editStateOfProjectUseCase(any(),any()) } throws Exception()
        //when
        ui.launchUi()
        //then
        verify { printer.displayLn(match { it.toString().contains("${Exception().message}") }) }
    }
}
