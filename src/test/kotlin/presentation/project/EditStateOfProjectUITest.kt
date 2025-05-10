package presentation.project

import helper.createState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.model.Project
import logic.model.State
import logic.project.EditStateOfProjectUseCase
import logic.project.GetAllProjectsUseCase
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import presentation.io.ConsoleReader
import presentation.io.Printer
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
    fun `should print exception message if getAllProjectsUseCase throws`() = runTest {
        val exception = Exception("Boom")
        coEvery { getAllProjectsUseCase() } throws exception

        ui.launchUi()

        coVerify { printer.displayLn("Boom") }
    }

    @Test
    fun `should print message when no projects exist`() = runTest {
        coEvery { getAllProjectsUseCase() } returns emptyList()

        ui.launchUi()

        coVerify { printer.displayLn("\nThere are no projects in the list.") }
    }

    @Test
    fun `should display projects and ask for inputs`() = runTest {
        val projects = listOf(
            Project(
                UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"),
                "name1",
                listOf(State(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1b"), "state1"))
            )
        )
        coEvery { getAllProjectsUseCase() } returns projects
        coEvery { reader.readInt() } returnsMany listOf(1, 1)
        coEvery { reader.readString() } returns "newName"

        ui.launchUi()

        coVerify {
            printer.displayLn("${1}- Project Name: name1 - States: [State(id=d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1b, name=state1)]")
            editStateOfProjectUseCase(
                UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a").toString(),
                State(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1b"), "newName")
            )
            printer.displayLn("\nState updated successfully.")
        }
    }

    @Test
    fun `should prompt again when input is null or blank`() = runTest {
        val projects = listOf(
            Project(
                UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", listOf(
                    createState()
                )
            )
        )
        coEvery { getAllProjectsUseCase() } returns projects

        coEvery { reader.readInt() } returnsMany listOf(
            null, null, null, 1, // read project #
            null, 1, // read state #
        )

        coEvery { reader.readString() } returnsMany listOf(
            null, "newStateName", // new name input retries
        )

        ui.launchUi()

        coVerify(exactly = 5) { printer.displayLn("\nInput cannot be empty.") }
        coVerify {
            editStateOfProjectUseCase(
                projects[0].id.toString(),
                projects[0].states[0].copy(name = "newStateName")
            )
        }
    }

    @ParameterizedTest
    @CsvSource("null,An error occurred.", "Update failed,Update failed", nullValues = ["null"])
    fun `should print error if editStateOfProjectUseCase throws`(errorMessage: String?, actualMessage: String) =
        runTest {
            val projects = listOf(Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", listOf()))
            coEvery { getAllProjectsUseCase() } returns projects
            coEvery { reader.readInt() } returnsMany listOf(1, 1)
            coEvery { reader.readString() } returns "newName"

            ui.launchUi()
            coVerify { printer.displayLn("\nState not found") }
        }
}
