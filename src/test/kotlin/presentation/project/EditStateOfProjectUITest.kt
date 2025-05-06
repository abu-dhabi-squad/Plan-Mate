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
        every { getAllProjectsUseCase() } throws exception

        ui.launchUi()

        verify { printer.displayLn("Boom") }
    }

    @Test
    fun `should print message when no projects exist`() = runTest{
        every { getAllProjectsUseCase() } returns emptyList()

        ui.launchUi()

        verify { printer.displayLn("There is no project in the list.") }
    }

    @Test
    fun `should display projects and ask for inputs`() = runTest{
        val projects = listOf(Project("id1", "name1", listOf(State("s1", "state1"))))
        every { getAllProjectsUseCase() } returns projects
        every { reader.readString() } returnsMany listOf("id1", "s1", "newName")

        ui.launchUi()

        verify {
            printer.displayLn("project id: id1 - project name: name1 - states: [State(id=s1, name=state1)]")
            editStateOfProjectUseCase("id1", State("s1", "newName"))
            printer.displayLn("State updated successfully.")
        }
    }

    @Test
    fun `should prompt again when input is null or blank`() = runTest{
        val projects = listOf(Project("id1", "name1", listOf()))
        every { getAllProjectsUseCase() } returns projects

        every { reader.readString() } returnsMany listOf(
            "", "  ", "\n", "validId", // projectId input retries
            "", "stateId",             // stateId input retries
            null, "newStateName"       // new name input retries
        )

        ui.launchUi()

        verify(exactly = 5) { printer.displayLn("Input cannot be empty.") }
        verify { editStateOfProjectUseCase("validId", State("stateId", "newStateName")) }
    }

    @ParameterizedTest
    @CsvSource("null,An error occurred.","Update failed,Update failed" , nullValues =["null"] )
    fun `should print error if editStateOfProjectUseCase throws`(errorMessage:String?,actualMessage:String) = runTest{
        val projects = listOf(Project("id1", "name1", listOf()))
        val exception = Exception(errorMessage)
        every { getAllProjectsUseCase() } returns projects
        every { reader.readString() } returnsMany listOf("id1", "sid1", "newName")
        every { editStateOfProjectUseCase(any(), any()) } throws exception

        ui.launchUi()
        verify { printer.displayLn(actualMessage) }
    }
}
