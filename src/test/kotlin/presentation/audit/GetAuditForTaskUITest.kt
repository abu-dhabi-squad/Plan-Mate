package presentation.audit

import io.mockk.every
import io.mockk.mockk
import logic.audit.GetAuditUseCase
import logic.helper.createTask
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import squad.abudhabi.logic.exceptions.EmptyList
import squad.abudhabi.logic.exceptions.WrongInputException
import squad.abudhabi.logic.model.Audit
import squad.abudhabi.logic.model.EntityType
import squad.abudhabi.logic.model.Project
import squad.abudhabi.logic.project.GetAllProjectsUseCase
import squad.abudhabi.logic.task.GetTasksByProjectIdUseCase
import squad.abudhabi.presentation.ui_io.ConsolePrinter
import squad.abudhabi.presentation.ui_io.InputReader
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.util.*
import kotlin.test.assertTrue

class GetAuditForTaskUITest {

    private val getAuditUseCase: GetAuditUseCase = mockk()
    private val reader = mockk<InputReader>()
    private val getAllProjectsUseCase = mockk<GetAllProjectsUseCase>(relaxed = true)
    private val printer = ConsolePrinter()
    private val getTasksByProjectIdUseCase = mockk<GetTasksByProjectIdUseCase>()
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
    fun `should show audit logs for selected task`() {
        val project = Project("p1", "Project A", listOf())
        val task = createTask(id = "t1", title = "Task A")
        val audits = listOf(
            Audit(UUID.randomUUID(), "admin", "t1", EntityType.TASK, "old", "new")
        )

        every { getAllProjectsUseCase() } returns listOf(project)
        every { reader.readInt() } returnsMany listOf(1, 1)
        every { getTasksByProjectIdUseCase("p1") } returns listOf(task)
        every { getAuditUseCase("t1") } returns audits

        ui.launchUi()

        val output = outContent.toString()
        assertTrue(output.contains("Entity: t1"))
    }

    @Test
    fun `should show error if no projects exist`() {

        every { getAllProjectsUseCase() } returns emptyList()

        ui.launchUi()

        val output = outContent.toString()
        assertTrue(output.contains("No projects available"))
    }

    @Test
    fun `should show error if no tasks exist for selected project`() {

        val project = Project("p1", "Project A", listOf())

        every { getAllProjectsUseCase() } returns listOf(project)
        every { reader.readInt() } returns 1
        every { getTasksByProjectIdUseCase("p1") } returns emptyList()

        ui.launchUi()

        val output = outContent.toString()
        assertTrue(output.contains("No tasks available"))
    }

    @Test
    fun `should handle null input for project`() {
        val project = Project("p1", "Project A", listOf())

        every { getAllProjectsUseCase() } returns listOf(project)
        every { reader.readInt() } returns null

        ui.launchUi()

        val output = outContent.toString()
        assertTrue(output.contains("Input cannot be empty"))
    }

    @Test
    fun `should handle invalid project index`() {
        val project = Project("p1", "Project A", listOf())

        every { getAllProjectsUseCase() } returns listOf(project)
        every { reader.readInt() } returns 5

        ui.launchUi()

        val output = outContent.toString()
        assertTrue(output.contains("Input cannot be out projects range"))
    }

    @Test
    fun `should handle null input for task`() {
        val project = Project("p1", "Project A", listOf())
        val task = createTask(id = "t1", title = "Task A")

        every { getAllProjectsUseCase() } returns listOf(project)
        every { reader.readInt() } returnsMany listOf(1, null)
        every { getTasksByProjectIdUseCase("p1") } returns listOf(task)

        ui.launchUi()

        val output = outContent.toString()
        assertTrue(output.contains("Input cannot be empty"))
    }

    @Test
    fun `should handle invalid task index`() {

        val project = Project("p1", "Project A", listOf())
        val task = createTask(id = "t1", title = "Task A")

        every { getAllProjectsUseCase() } returns listOf(project)
        every { reader.readInt() } returnsMany listOf(1, 5)
        every { getTasksByProjectIdUseCase("p1") } returns listOf(task)

        ui.launchUi()

        val output = outContent.toString()
        assertTrue(output.contains("Input cannot be out tasks range"))
    }

    @Test
    fun `should show message when no audit logs found`() {
        val project = Project("p1", "Project A", listOf())
        val task = createTask(id = "t1", title = "Task A")

        every { getAllProjectsUseCase() } returns listOf(project)
        every { reader.readInt() } returnsMany listOf(1, 1)
        every { getTasksByProjectIdUseCase("p1") } returns listOf(task)
        every { getAuditUseCase("t1") } returns emptyList()

        ui.launchUi()

        val output = outContent.toString()
        assertTrue(output.contains("No audit logs found"))
    }

    @Test
    fun `should handle WrongInputException from use case`() {
        val project = Project("p1", "Project A", listOf())
        val task = createTask(id = "t1", title = "Task A")

        every { getAllProjectsUseCase() } returns listOf(project)
        every { reader.readInt() } returnsMany listOf(1, 1)
        every { getTasksByProjectIdUseCase("p1") } returns listOf(task)
        every { getAuditUseCase("t1") } throws WrongInputException()

        ui.launchUi()

        val output = outContent.toString()
        assertTrue(output.contains("Invalid entity ID"))
    }

    @Test
    fun `should handle EmptyList exception from use case`() {
        val project = Project("p1", "Project A", listOf())
        val task = createTask(id = "t1", title = "Task A")

        every { getAllProjectsUseCase() } returns listOf(project)
        every { reader.readInt() } returnsMany listOf(1, 1)
        every { getTasksByProjectIdUseCase("p1") } returns listOf(task)
        every { getAuditUseCase("t1") } throws EmptyList()

        ui.launchUi()

        val output = outContent.toString()
        assertTrue(output.contains("No audit logs found"))
    }

    @Test
    fun `should handle unexpected exception`() {
        val project = Project("p1", "Project A", listOf())
        val task = createTask(id = "t1", title = "Task A")

        every { getAllProjectsUseCase() } returns listOf(project)
        every { reader.readInt() } returnsMany listOf(1, 1)
        every { getTasksByProjectIdUseCase("p1") } returns listOf(task)
        every { getAuditUseCase("t1") } throws Exception()

        ui.launchUi()

        val output = outContent.toString()
        assertTrue(output.contains("error"))
    }

    @Test
    fun `should throw exception when get all projects fails`(){

        every { getAllProjectsUseCase() } throws Exception()

        ui.launchUi()

        val output = outContent.toString()
        assertTrue(output.contains("Failed to fetch projects"))
    }
}
