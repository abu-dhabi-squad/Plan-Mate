package presentation.audit

import helper.createAudit
import helper.createProject
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import logic.audit.GetAuditUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import logic.project.GetAllProjectsUseCase
import presentation.io.Printer
import presentation.utils.PromptService
import presentation.utils.extensions.showAuditLogs

class GetAuditForProjectUITest {
    private val promptService: PromptService = mockk(relaxed = true)
    private val getAuditUseCase: GetAuditUseCase = mockk(relaxed = true)
    private val printer: Printer = mockk(relaxed = true)
    private val getAllProjectsUseCase: GetAllProjectsUseCase = mockk(relaxed = true)
    private lateinit var ui: GetAuditForProjectUI

    @BeforeEach
    fun setUp() {
        ui = GetAuditForProjectUI(
            promptService = promptService,
            printer = printer,
            getAuditUseCase = getAuditUseCase,
            getAllProjectsUseCase = getAllProjectsUseCase
        )
    }

    @Test
    fun `launchUi should show audit logs for valid project`() = runTest {
        //Given
        val project = createProject(name = "Project A")
        val projects = listOf(project)
        val audits = listOf(createAudit(entityId = project.projectId))
        coEvery { getAllProjectsUseCase() } returns projects
        every { promptService.promptSelectionIndex(any(), any()) } returns 0
        coEvery { getAuditUseCase(any()) } returns audits
        //When
        ui.launchUi()
        //Then
        verify {
            projects.forEachIndexed { index, project ->
                printer.displayLn(match {
                    it.toString().contains(project.projectName)
                })
            }
        }
        verify { audits.showAuditLogs(printer) }
    }

    @Test
    fun `launchUi should show no projects available when no projects exist`() = runTest {
        //Given
        coEvery { getAllProjectsUseCase() } returns emptyList()
        //When
        ui.launchUi()
        //Then
        verify { printer.displayLn(match { it.toString().contains("No projects available") }) }
    }

    @Test
    fun `launchUi should print error message when getAllProjectsUseCase throw exception`() = runTest {
        //Given
        coEvery { getAllProjectsUseCase() } throws Exception()
        //When
        ui.launchUi()
        //Then
        verify { printer.displayLn(match { it.toString().contains("${Exception().message}") }) }
    }

    @Test
    fun `launchUi should print error message when getAuditUseCase throw exception`() = runTest {
        //Given
        val project = createProject(name = "Project A")
        val projects = listOf(project)
        coEvery { getAllProjectsUseCase() } returns projects
        every { promptService.promptSelectionIndex(any(), any()) } returns 0
        coEvery { getAuditUseCase(any()) } throws Exception()
        //When
        ui.launchUi()
        //Then
        verify { printer.displayLn(match { it.toString().contains("${Exception().message}") }) }
    }
}