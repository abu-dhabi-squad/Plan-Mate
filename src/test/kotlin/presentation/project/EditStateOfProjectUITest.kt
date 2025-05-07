package presentation.project

import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import squad.abudhabi.logic.model.Project
import squad.abudhabi.logic.model.State
import logic.project.EditStateOfProjectUseCase
import logic.project.GetAllProjectsUseCase
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import presentation.ui_io.ConsoleReader
import presentation.ui_io.Printer
import java.util.*
import kotlin.test.BeforeTest

class EditStateOfProjectUITest {

    private val printer: Printer = mockk(relaxed = true)
    private val reader: ConsoleReader = mockk(relaxed = true)
    private val editStateOfProjectUseCase: EditStateOfProjectUseCase = mockk(relaxed = true)
    private val getAllProjectsUseCase: GetAllProjectsUseCase = mockk(relaxed = true)
    private lateinit var ui: EditStateOfProjectUI

    @BeforeTest
    fun setup() {
        ui = EditStateOfProjectUI(editStateOfProjectUseCase, getAllProjectsUseCase, reader, printer)
    }

    @Test
    fun `should print exception message if getAllProjectsUseCase throws`() = runTest{
        val exception = Exception("Boom")
        coEvery { getAllProjectsUseCase() } throws exception

        ui.launchUi()

        coVerify { printer.displayLn("Boom") }
    }

    @Test
    fun `should print message when no projects exist`() = runTest{
        coEvery { getAllProjectsUseCase() } returns emptyList()

        ui.launchUi()

        coVerify { printer.displayLn("There is no project in the list.") }
    }

    @Test
    fun `should display projects and ask for inputs`() = runTest{
        val projects = listOf(Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", listOf(State("s1", "state1"))))
        coEvery { getAllProjectsUseCase() } returns projects
        coEvery { reader.readString() } returnsMany listOf(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a").toString(), "s1", "newName")

        ui.launchUi()

        coVerify {
            printer.displayLn("project id: d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a - project name: name1 - states: [State(id=s1, name=state1)]")
            editStateOfProjectUseCase(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a").toString(), State("s1", "newName"))
            printer.displayLn("State updated successfully.")
        }
    }

    @Test
    fun `should prompt again when input is null or blank`() = runTest{
        val projects = listOf(Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", listOf()))
        coEvery { getAllProjectsUseCase() } returns projects

        coEvery { reader.readString() } returnsMany listOf(
            "", "  ", "\n", "validId", // projectId input retries
            "", "stateId",             // stateId input retries
            null, "newStateName"       // new name input retries
        )

        ui.launchUi()

        coVerify(exactly = 5) { printer.displayLn("Input cannot be empty.") }
        coVerify { editStateOfProjectUseCase("validId", State("stateId", "newStateName")) }
    }

    @ParameterizedTest
    @CsvSource("null,An error occurred.","Update failed,Update failed" , nullValues =["null"] )
    fun `should print error if editStateOfProjectUseCase throws`(errorMessage:String?,actualMessage:String) = runTest{
        val projects = listOf(Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", listOf()))
        val exception = Exception(errorMessage)
        coEvery { getAllProjectsUseCase() } returns projects
        coEvery { reader.readString() } returnsMany listOf("id1", "sid1", "newName")
        coEvery { editStateOfProjectUseCase(any(), any()) } throws exception

        ui.launchUi()
        coVerify { printer.displayLn(actualMessage) }
    }
}
