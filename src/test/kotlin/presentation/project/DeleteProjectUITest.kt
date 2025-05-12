package presentation.project

import helper.createProject
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import logic.audit.CreateAuditUseCase
import logic.project.DeleteProjectUseCase
import logic.project.GetAllProjectsUseCase
import logic.user.GetLoggedUserUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.io.Printer
import presentation.presentation.utils.PromptService


class DeleteProjectUITest {
    private val deleteProjectUseCase: DeleteProjectUseCase = mockk(relaxed = true)
    private val getAllProjectsUseCase: GetAllProjectsUseCase = mockk(relaxed = true)
    private val printer: Printer = mockk(relaxed = true)
    private val promptService: PromptService = mockk(relaxed = true)
    private val createAuditUseCase: CreateAuditUseCase = mockk(relaxed = true)
    private val getLoggedUserUseCase: GetLoggedUserUseCase = mockk(relaxed = true)
    private lateinit var ui: DeleteProjectUI

    @BeforeEach
    fun setUp() {
        ui = DeleteProjectUI(
            deleteProjectUseCase = deleteProjectUseCase,
            getAllProjectsUseCase = getAllProjectsUseCase,
            printer = printer,
            promptService = promptService,
            createAuditUseCase = createAuditUseCase,
            getLoggedUserUseCase = getLoggedUserUseCase,
        )
    }

    @Test
    fun `launchUi should delete project successfully when nothing went wrong`() = runTest {
        //Given
        val project = createProject()
        val projects = listOf(project)
        coEvery { getAllProjectsUseCase() } returns projects
        every { promptService.promptSelectionIndex(any(), any()) } returns 0
        //When
        ui.launchUi()
        //Then
        coVerify { deleteProjectUseCase(any()) }
        coVerify { createAuditUseCase(any()) }
        verify { printer.displayLn(match { it.toString().contains("deleted") }) }
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
    fun `launchUi should show error message when deleteProjectUseCase throw exception`() = runTest {
        //Given
        val project = createProject()
        val projects = listOf(project)
        coEvery { getAllProjectsUseCase() } returns projects
        every { promptService.promptSelectionIndex(any(), any()) } returns 0
        coEvery { deleteProjectUseCase(any()) } throws Exception()
        //When
        ui.launchUi()
        //Then
        verify { printer.displayLn(match { it.toString().contains("${Exception().message}") }) }
    }

    @Test
    fun `launchUi should show error message when createAuditUseCase throw exception`() = runTest {
        //Given
        val project = createProject()
        val projects = listOf(project)
        coEvery { getAllProjectsUseCase() } returns projects
        every { promptService.promptSelectionIndex(any(), any()) } returns 0
        coEvery { createAuditUseCase(any()) } throws Exception()
        //When
        ui.launchUi()
        //Then
        verify { printer.displayLn(match { it.toString().contains("${Exception().message}") }) }
    }
}