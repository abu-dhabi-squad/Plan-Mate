package presentation.audit

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
import presentation.io.ConsolePrinter
import presentation.io.InputReader
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.util.UUID
import kotlin.test.assertTrue

class GetAuditForProjectUITest{

    private val getAuditUseCase: GetAuditUseCase = mockk()
    private val reader = mockk<InputReader>()
    private val printer = ConsolePrinter()
    private val getAllProjectsUseCase = mockk<GetAllProjectsUseCase>()
    private val outContent = ByteArrayOutputStream()
    private lateinit var ui: GetAuditForProjectUI

    @BeforeEach
    fun setUp() {
        System.setOut(PrintStream(outContent))
        ui = GetAuditForProjectUI(
            printer = printer,
            reader = reader,
            getAuditUseCase = getAuditUseCase,
            getAllProjectsUseCase = getAllProjectsUseCase
        )
    }

    @Test
    fun `should show audit logs for valid project`() = runTest{

        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "Project A", states = listOf())
        val audits = listOf(
            Audit(UUID.randomUUID(), "admin", "p1", EntityType.PROJECT, "old", "new")
        )

        coEvery { getAllProjectsUseCase() } returns listOf(project)
        coEvery { reader.readInt() } returns 1
        coEvery { getAuditUseCase(any()) } returns audits

        ui.launchUi()

        val output = outContent.toString()
        assertTrue(output.contains("old") && output.contains("new"))
    }

    @Test
    fun `should show error if no projects exist`() = runTest{
        coEvery { getAllProjectsUseCase() } returns emptyList()

        ui.launchUi()

        val output = outContent.toString()
        assertTrue(output.contains("No projects available"))
    }

    @Test
    fun `should handle null input selection`() = runTest{
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "Project A", states = listOf())

        coEvery { getAllProjectsUseCase() } returns listOf(project)
        coEvery { reader.readInt() } returns null

        ui.launchUi()

        val output = outContent.toString()
        assertTrue(output.contains("Input cannot be empty"))
    }

    @Test
    fun `should handle invalid project index`() = runTest{

        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "Project A", states = listOf())

        coEvery { getAllProjectsUseCase() } returns listOf(project)
        coEvery { reader.readInt() } returns 5

        ui.launchUi()

        val output = outContent.toString()
        assertTrue(output.contains("Input cannot be out projects range"))
    }

    @Test
    fun `should show message when no audit logs found`() = runTest{

        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "Project A", states = listOf())

        coEvery { getAllProjectsUseCase() } returns listOf(project)
        coEvery { reader.readInt() } returns 1
        coEvery { getAuditUseCase(any()) } returns emptyList()

        ui.launchUi()

        val output = outContent.toString()
        assertTrue(output.contains("No audit logs found"))
    }

    @Test
    fun `should handle WrongInputException gracefully`() = runTest{

        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "Project A", states = listOf())

        coEvery { getAllProjectsUseCase() } returns listOf(project)
        coEvery { reader.readInt() } returns 1
        coEvery { getAuditUseCase(any()) } throws WrongInputException()

        ui.launchUi()

        val output = outContent.toString()
        assertTrue(output.contains("Invalid entity ID"))
    }

    @Test
    fun `should handle EmptyList exception gracefully`() = runTest{

        //val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "Project A", states = listOf())
        val project= Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"),"", listOf())
        coEvery { getAllProjectsUseCase() } returns listOf(project)
        coEvery { reader.readInt() } returns 1
        coEvery { getAuditUseCase(any()) } throws EmptyList()

        ui.launchUi()

        val output = outContent.toString()
        assertTrue(output.contains("No audit logs found"))
    }

    @Test
    fun `should handle unexpected exception`() = runTest{

        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "Project A", states = listOf())

        coEvery { getAllProjectsUseCase() } returns listOf(project)
        coEvery { reader.readInt() } returns 1
        coEvery { getAuditUseCase("p1") } throws Exception()

        ui.launchUi()

        val output = outContent.toString()
        assertTrue(output.contains("Error"))
    }
}
