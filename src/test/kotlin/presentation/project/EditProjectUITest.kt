package presentation.project

import helper.createProject
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import logic.project.EditProjectUseCase
import logic.project.GetAllProjectsUseCase
import presentation.io.Printer
import presentation.utils.PromptUtils
import kotlin.test.BeforeTest
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class EditProjectUITest {
    private val editProjectUseCase: EditProjectUseCase = mockk(relaxed = true)
    private val getAllProjectsUseCase: GetAllProjectsUseCase = mockk(relaxed = true)
    private val promptUtils: PromptUtils = mockk(relaxed = true)
    private val printer: Printer = mockk(relaxed = true)
    private lateinit var ui: EditProjectUI

    @BeforeTest
    fun setup() {
        ui = EditProjectUI(
            editProjectUseCase = editProjectUseCase,
            getAllProjectsUseCase = getAllProjectsUseCase,
            promptUtils = promptUtils,
            printer = printer
        )
    }

    @Test
    fun `launchUI should edit successfully when nothing went wrong`() = runTest{
        //given
        val project = createProject()
        val projects = listOf(project)
        coEvery { getAllProjectsUseCase() } returns projects
        every { promptUtils.promptSelectionIndex(any(),any()) } returns 0
        every { promptUtils.promptNonEmptyString(any()) } returns "newName"
        //when
        ui.launchUi()
        //then
        coVerify { editProjectUseCase(any(),any()) }
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
    fun `launchUI should display Exception message editProjectUseCase when throw Exception`() = runTest{
        //given
        val project = createProject()
        val projects = listOf(project)
        coEvery { getAllProjectsUseCase() } returns projects
        every { promptUtils.promptSelectionIndex(any(),any())} returns 0
        coEvery { editProjectUseCase(any(),any()) } throws Exception()
        //when
        ui.launchUi()
        //then
        verify { printer.displayLn(match { it.toString().contains("${Exception().message}") }) }
    }
}
