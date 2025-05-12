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
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
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
        every { promptService.promptNonEmptyInt(any()) } returns 1 andThen 1
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

    @ParameterizedTest
    @ValueSource(ints = [0, 100])
    fun `launchUi should show not found message when entering wrong project number`(projectNum: Int) = runTest {
        //Given
        val project = createProject()
        val projects = listOf(project)
        coEvery { getAllProjectsUseCase() } returns projects
        every { promptService.promptNonEmptyInt(any()) } returns projectNum
        //When
        ui.launchUi()
        //Then
        verify { printer.displayLn(match { it.toString().contains("not found") }) }
    }

    @ParameterizedTest
    @ValueSource(ints = [0, 100])
    fun `launchUi should show not found message when entering wrong state number`(projectNum: Int) = runTest {
        //Given
        val project = createProject()
        val projects = listOf(project)
        coEvery { getAllProjectsUseCase() } returns projects
        every { promptService.promptNonEmptyInt(any()) } returns 1 andThen projectNum
        //When
        ui.launchUi()
        //Then
        verify { printer.displayLn(match { it.toString().contains("not found") }) }
    }

    @Test
    fun `launchUI should display Exception message editStateOfProjectUseCase when throw Exception`() = runTest{
        //given
        val project = createProject(taskStates = listOf(TaskState(stateName = "state1")))
        val projects = listOf(project)
        coEvery { getAllProjectsUseCase() } returns projects
        every { promptService.promptNonEmptyInt(any()) } returns 1 andThen 1
        every { promptService.promptNonEmptyString(any()) } returns "newName"
        coEvery { editStateOfProjectUseCase(any(),any()) } throws Exception()
        //when
        ui.launchUi()
        //then
        verify { printer.displayLn(match { it.toString().contains("${Exception().message}") }) }
    }
}
