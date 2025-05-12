package presentation.audit

import helper.createAudit
import helper.createProject
import helper.createTask
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import logic.audit.GetAuditUseCase
import logic.project.GetAllProjectsUseCase
import logic.task.GetTasksByProjectIdUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.io.Printer
import presentation.presentation.utils.PromptService
import presentation.presentation.utils.extensions.showAuditLogs

class GetAuditForTaskUITest {

    private val printer: Printer = mockk(relaxed = true)
    private val promptService: PromptService = mockk(relaxed = true)
    private val getAuditUseCase: GetAuditUseCase = mockk(relaxed = true)
    private val getAllProjectsUseCase: GetAllProjectsUseCase = mockk(relaxed = true)
    private val getTasksByProjectIdUseCase: GetTasksByProjectIdUseCase = mockk(relaxed = true)
    private lateinit var ui: GetAuditForTaskUI

    @BeforeEach
    fun setUp() {
        ui = GetAuditForTaskUI(
            printer = printer,
            promptService = promptService,
            getAuditUseCase = getAuditUseCase,
            getAllProjectsUseCase = getAllProjectsUseCase,
            getTasksByProjectIdUseCase = getTasksByProjectIdUseCase
        )
    }

    @Test
    fun `launchUi should show audit logs for valid task in valid project`() = runTest {
        //Given
        val project = createProject(name = "Project A")
        val projects = listOf(project)
        val task = createTask(projectId = project.projectId)
        val tasks = listOf(task)
        val audits = listOf(createAudit(entityId = task.taskId))
        coEvery { getAllProjectsUseCase() } returns projects
        every { promptService.promptSelectionIndex(any(), any()) } returns 0
        coEvery { getTasksByProjectIdUseCase(projectId = project.projectId) } returns tasks
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

        verify {
            tasks.forEachIndexed { index, task -> printer.displayLn(match { it.toString().contains(task.title) }) }
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
    fun `launchUi should print error message when getTasksByProjectIdUseCase throw exception`() = runTest {
        //Given
        val project = createProject(name = "Project A")
        val projects = listOf(project)
        coEvery { getAllProjectsUseCase() } returns projects
        every { promptService.promptSelectionIndex(any(), any()) } returns 0
        coEvery { getTasksByProjectIdUseCase(projectId = project.projectId) } throws Exception()
        //When
        ui.launchUi()
        //Then
        verify { printer.displayLn(match { it.toString().contains("${Exception().message}") }) }
    }

    @Test
    fun `launchUi should print No tasks available message when getTasksByProjectIdUseCase returns empty list`() =
        runTest {
            //Given
            val project = createProject(name = "Project A")
            val projects = listOf(project)
            coEvery { getAllProjectsUseCase() } returns projects
            every { promptService.promptSelectionIndex(any(), any()) } returns 0
            coEvery { getTasksByProjectIdUseCase(projectId = project.projectId) } returns emptyList()
            //When
            ui.launchUi()
            //Then
            verify { printer.displayLn(match { it.toString().contains("No tasks available") }) }
        }

    @Test
    fun `launchUi should print error message when getAuditUseCase throw exception`() = runTest {
        //Given
        val project = createProject(name = "Project A")
        val projects = listOf(project)
        val task = createTask(projectId = project.projectId)
        val tasks = listOf(task)
        coEvery { getAllProjectsUseCase() } returns projects
        every { promptService.promptSelectionIndex(any(), any()) } returns 0
        coEvery { getTasksByProjectIdUseCase(projectId = project.projectId) } returns tasks
        coEvery { getAuditUseCase(any()) } throws Exception()
        //When
        ui.launchUi()
        //Then
        verify { printer.displayLn(match { it.toString().contains("${Exception().message}") }) }
    }
}