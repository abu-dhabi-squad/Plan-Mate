package presentation.project

import helper.createProject
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import logic.project.AddStateToProjectUseCase
import logic.project.GetAllProjectsUseCase
import presentation.io.Printer
import presentation.utils.PromptService

class AddTaskStateToProjectUITest {
    private val addStateToProjectUseCase: AddStateToProjectUseCase = mockk(relaxed = true)
    private val getAllProjectsUseCase: GetAllProjectsUseCase = mockk(relaxed = true)
    private val promptService: PromptService = mockk(relaxed = true)
    private val printer: Printer = mockk(relaxed = true)
    private lateinit var ui: AddStateToProjectUI

    @BeforeEach
    fun setUp() {
        ui = AddStateToProjectUI(
            addStateToProjectUseCase = addStateToProjectUseCase,
            getAllProjectsUseCase = getAllProjectsUseCase,
            promptService = promptService,
            printer = printer
        )
    }

    @Test
    fun `launchUi should add state successfully when there is nothing went wrong`() = runTest {
        //Given
        val project = createProject(name = "Project A")
        val projects = listOf(project)
        coEvery { getAllProjectsUseCase() } returns projects
        every { promptService.promptSelectionIndex(any(), any()) } returns 0
        every { promptService.promptNonEmptyString(any()) } returns "newName"
        //When
        ui.launchUi()
        //Then
        verify {
            projects.forEachIndexed { index, project ->
                printer.display(match { it.toString().contains(project.projectName) })
                project.taskStates.forEachIndexed { stateIndex, state ->
                    printer.display(match { it.toString().contains(state.stateName) })
                }
            }
        }
        coVerify { addStateToProjectUseCase(project.projectId, any()) }
        verify { printer.displayLn(match { it.toString().contains("\"newName\" added to project") }) }
    }

    @Test
    fun `launchUi should show no projects available when no projects exist`() = runTest {
        //Given
        coEvery { getAllProjectsUseCase() } returns emptyList()
        //When
        ui.launchUi()
        //Then
        verify { printer.displayLn(match { it.toString().contains("no projects") }) }
    }

    @Test
    fun `launchUi should show error message when getAllProjectsUseCase throw exception`() = runTest {
        //Given
        coEvery { getAllProjectsUseCase() } throws Exception()
        //When
        ui.launchUi()
        //Then
        verify { printer.displayLn(match { it.toString().contains("${Exception().message}") }) }
    }

    @Test
    fun `launchUi should show error message when  throw exception`() = runTest {
        //Given
        val project = createProject()
        val projects = listOf(project)
        coEvery { getAllProjectsUseCase() } returns projects
        every { promptService.promptSelectionIndex(any(), any()) } returns 0
        every { promptService.promptNonEmptyString(any()) } returns "newName"
        coEvery { addStateToProjectUseCase(project.projectId, any()) } throws Exception()
        //When
        ui.launchUi()
        //Then
        verify { printer.displayLn(match { it.toString().contains("${Exception().message}") }) }
    }
}