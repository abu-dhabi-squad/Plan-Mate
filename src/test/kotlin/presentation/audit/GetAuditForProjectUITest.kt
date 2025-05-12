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
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import presentation.io.Printer
import presentation.presentation.utils.PromptService
import presentation.presentation.utils.extensions.showAuditLogs

class GetAuditForProjectUITest{

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
    fun `launchUi should show audit logs for valid project`() = runTest{
        //Given
        val project = createProject(name = "Project A")
        val projects = listOf(project)
        val audits = listOf(createAudit(entityId = project.projectId))
        coEvery { getAllProjectsUseCase() } returns projects
        every { promptService.promptNonEmptyInt(any()) } returns 1
        coEvery { getAuditUseCase(any()) } returns audits
        //When
        ui.launchUi()
        //Then
        verify { projects.forEachIndexed { index, project ->  printer.displayLn(match { it.toString().contains(project.projectName) })} }
        verify { audits.showAuditLogs(printer) }
    }

    @Test
    fun `launchUi should show no projects available when no projects exist`() = runTest{
        //Given
        coEvery { getAllProjectsUseCase() } returns emptyList()
        //When
        ui.launchUi()
        //Then
        verify { printer.displayLn( match{ it.toString().contains("No projects available")}) }
    }

    @Test
    fun `launchUi should print error message when getAllProjectsUseCase throw exception`() = runTest{
        //Given
        coEvery { getAllProjectsUseCase() } throws Exception()
        //When
        ui.launchUi()
        //Then
        verify { printer.displayLn( match{ it.toString().contains("${Exception().message}")}) }
    }

    @ParameterizedTest
    @ValueSource(ints = [0,100])
    fun `launchUi should show audit logs when user enter wrong project number`(projectNum: Int) = runTest{
        //Given
        val project = createProject(name = "Project A")
        val projects = listOf(project)
        coEvery { getAllProjectsUseCase() } returns projects
        every { promptService.promptNonEmptyInt(any()) } returns projectNum
        //When
        ui.launchUi()
        //Then
        verify { projects.forEachIndexed { index, project ->  printer.displayLn(match { it.toString().contains("out of projects range") })} }
    }

    @Test
    fun `launchUi should print error message when getAuditUseCase throw exception`() = runTest{
        //Given
        val project = createProject(name = "Project A")
        val projects = listOf(project)
        coEvery { getAllProjectsUseCase() } returns projects
        every { promptService.promptNonEmptyInt(any()) } returns 1
        coEvery { getAuditUseCase(any()) } throws Exception()
        //When
        ui.launchUi()
        //Then
        verify { printer.displayLn( match{ it.toString().contains("${Exception().message}")}) }
    }
}
