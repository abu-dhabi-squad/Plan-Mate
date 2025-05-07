package presentation.audit

import helper.createTask
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.audit.GetAuditUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import logic.exceptions.EmptyList
import logic.exceptions.WrongInputException
import logic.model.Audit
import logic.model.EntityType
import logic.model.Project
import logic.project.GetAllProjectsUseCase
import logic.task.GetTasksByProjectIdUseCase
import presentation.ui_io.ConsolePrinter
import presentation.ui_io.InputReader
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.util.*
import kotlin.test.assertTrue

class GetAuditForTaskUITest {

    private val getAuditUseCase: GetAuditUseCase = mockk(relaxed = true)
    private val reader = mockk<InputReader>(relaxed = true)
    private val getAllProjectsUseCase = mockk<GetAllProjectsUseCase>(relaxed = true)
    private val printer = ConsolePrinter()
    private val getTasksByProjectIdUseCase = mockk<GetTasksByProjectIdUseCase>(relaxed = true)
    private val outContent = ByteArrayOutputStream()
    private lateinit var ui: GetAuditForTaskUI

    @BeforeEach
    fun setUp() {
        System.setOut(PrintStream(outContent))
        ui = GetAuditForTaskUI(
            printer = printer,
            reader = reader,
            getAuditUseCase = getAuditUseCase,
            getAllProjectsUseCase = getAllProjectsUseCase,
            getTasksByProjectIdUseCase = getTasksByProjectIdUseCase
        )
    }

    @Test
    fun `should show audit logs for selected task`() = runTest{
        val uuid=UUID.randomUUID()
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "Project A", listOf())
        val task = createTask(id = uuid, title = "Task A")
        val audits = listOf(
            Audit(UUID.randomUUID(), "admin", "t1", EntityType.TASK, "old", "new")
        )

        coEvery { getAllProjectsUseCase() } returns listOf(project)
        coEvery { reader.readInt() } returnsMany listOf(1, 1)
        coEvery { getTasksByProjectIdUseCase(project.id.toString()) } returns listOf(task)
        coEvery { getAuditUseCase(any()) } returns audits

        ui.launchUi()

        val output = outContent.toString()
        assertTrue(output.contains("Entity: t1"))
    }

    @Test
    fun `should show error if no projects exist`() = runTest{

        coEvery { getAllProjectsUseCase() } returns emptyList()

        ui.launchUi()

        val output = outContent.toString()
        assertTrue(output.contains("No projects available"))
    }

    @Test
    fun `should show error if no tasks exist for selected project`() = runTest{

        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "Project A", listOf())

        coEvery { getAllProjectsUseCase() } returns listOf(project)
        coEvery { reader.readInt() } returns 1
        coEvery { getTasksByProjectIdUseCase(project.id.toString()) } returns emptyList()

        ui.launchUi()

        val output = outContent.toString()
        assertTrue(output.contains("No tasks available"))
    }

    @Test
    fun `should handle null input for project`() = runTest{
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "Project A", listOf())

        coEvery { getAllProjectsUseCase() } returns listOf(project)
        coEvery { reader.readInt() } returns null

        ui.launchUi()

        val output = outContent.toString()
        assertTrue(output.contains("Input cannot be empty"))
    }

    @Test
    fun `should handle invalid project index`() = runTest{
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "Project A", listOf())

        coEvery { getAllProjectsUseCase() } returns listOf(project)
        coEvery { reader.readInt() } returns 5

        ui.launchUi()

        val output = outContent.toString()
        assertTrue(output.contains("Input cannot be out projects range"))
    }

    @Test
    fun `should handle null input for task`() = runTest{
        val uuid=UUID.randomUUID()
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "Project A", listOf())
        val task = createTask(id = uuid, title = "Task A")

        coEvery { getAllProjectsUseCase() } returns listOf(project)
        coEvery { reader.readInt() } returnsMany listOf(1, null)
        coEvery { getTasksByProjectIdUseCase(project.id.toString()) } returns listOf(task)

        ui.launchUi()

        val output = outContent.toString()
        assertTrue(output.contains("Input cannot be empty"))
    }

    @Test
    fun `should handle invalid task index`() = runTest{
        val uuid=UUID.randomUUID()
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "Project A", listOf())
        val task = createTask(id = uuid, title = "Task A")

        coEvery { getAllProjectsUseCase() } returns listOf(project)
        coEvery { reader.readInt() } returnsMany listOf(1, 5)
        coEvery { getTasksByProjectIdUseCase(project.id.toString()) } returns listOf(task)

        ui.launchUi()

        val output = outContent.toString()
        assertTrue(output.contains("Input cannot be out tasks range"))
    }

    @Test
    fun `should show message when no audit logs found`() = runTest{
        val uuid=UUID.randomUUID()
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "Project A", listOf())
        val task = createTask(id = uuid, title = "Task A")

        coEvery { getAllProjectsUseCase() } returns listOf(project)
        coEvery { reader.readInt() } returnsMany listOf(1, 1)
        coEvery { getTasksByProjectIdUseCase(project.id.toString()) } returns listOf(task)
        coEvery { getAuditUseCase(any()) } returns emptyList()

        ui.launchUi()

        val output = outContent.toString()
        assertTrue(output.contains("No audit logs found"))
    }

    @Test
    fun `should handle WrongInputException from use case`() = runTest{
        val uuid=UUID.randomUUID()
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "Project A", listOf())
        val task = createTask(id = uuid, title = "Task A")

        coEvery { getAllProjectsUseCase() } returns listOf(project)
        coEvery { reader.readInt() } returnsMany listOf(1, 1)
        coEvery { getTasksByProjectIdUseCase(project.id.toString()) } returns listOf(task)
        coEvery { getAuditUseCase(any()) } throws WrongInputException()

        ui.launchUi()

        val output = outContent.toString()
        assertTrue(output.contains("Invalid entity ID input. Please try again."))
    }

    @Test
    fun `should handle EmptyList exception from use case`() = runTest{
        val uuid=UUID.randomUUID()
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "Project A", listOf())
        val task = createTask(id = uuid, title = "Task A")

        coEvery { getAllProjectsUseCase() } returns listOf(project)
        coEvery { reader.readInt() } returnsMany listOf(1, 1)
        coEvery { getTasksByProjectIdUseCase(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a").toString()) } returns listOf(task)
        coEvery { getAuditUseCase("t1") } throws EmptyList()

        ui.launchUi()

        val output = outContent.toString()
        assertTrue(output.contains("No audit logs found"))
    }

    @Test
    fun `should handle unexpected exception`() = runTest{
        val uuid=UUID.randomUUID()
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "Project A", listOf())
        val task = createTask(id = uuid, title = "Task A")

        coEvery { getAllProjectsUseCase() } returns listOf(project)
        coEvery { reader.readInt() } returnsMany listOf(1, 1)
        coEvery { getTasksByProjectIdUseCase(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a").toString()) } returns listOf(task)
        coEvery { getAuditUseCase(any()) } throws Exception()

        ui.launchUi()

        val output = outContent.toString()
        assertTrue(output.contains("error"))
    }

    @Test
    fun `should throw exception when get all projects fails`() = runTest{

        coEvery { getAllProjectsUseCase() } throws Exception()

        ui.launchUi()

        val output = outContent.toString()
        assertTrue(output.contains("Failed to fetch projects"))
    }
}
