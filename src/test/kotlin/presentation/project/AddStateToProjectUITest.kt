package presentation.project

import helper.createProject
import helper.createState
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import logic.exceptions.DuplicateStateException
import logic.exceptions.NoProjectsFoundException
import logic.project.AddStateToProjectUseCase
import logic.project.GetAllProjectsUseCase
import presentation.io.InputReader
import presentation.io.Printer

class AddStateToProjectUITest{
    private lateinit var addStateToProjectUI: AddStateToProjectUI
    private lateinit var reader: InputReader
    private lateinit var printer: Printer
    private lateinit var getAllProjectsUseCase: GetAllProjectsUseCase
    private lateinit var addStateToProjectUseCase: AddStateToProjectUseCase

    @BeforeEach
    fun setUp() {
        addStateToProjectUseCase = mockk()
        reader = mockk()
        printer = mockk(relaxed = true)
        getAllProjectsUseCase = mockk(relaxed = true)
        addStateToProjectUI = AddStateToProjectUI(addStateToProjectUseCase, getAllProjectsUseCase, reader, printer)
    }

    @Test
    fun `should print exception message if getAllProjectsUseCase throws`() = runTest {
        val exception = NoProjectsFoundException()
        coEvery { getAllProjectsUseCase() } throws exception

        addStateToProjectUI.launchUi()

        coVerify { printer.displayLn(exception.message) }
    }

    @Test
    fun `should print message when no projects exist`() = runTest {
        coEvery { getAllProjectsUseCase() } returns emptyList()

        addStateToProjectUI.launchUi()

        coVerify { printer.displayLn("\nThere are no projects in the list.") }
    }

    @Test
    fun `should call use case and print success when inputs are valid`() = runTest{
        val projects = listOf(
            createProject(name = "name1", states = listOf(createState(name = "TODO"))),
        )
        coEvery { getAllProjectsUseCase() } returns projects
        coEvery { reader.readInt() } returns 1
        coEvery { reader.readString() } returns "In Progress"

        coEvery { addStateToProjectUseCase(projects[0].id.toString(), match { it.name == "In Progress" }) } just Runs

        addStateToProjectUI.launchUi()

        coVerify { addStateToProjectUseCase(projects[0].id.toString(), match { it.name == "In Progress" }) }
        coVerify { printer.displayLn("State \"In Progress\" added to project \"name1\" successfully.") }
    }

    @Test
    fun `should print error when duplicate state exception is thrown`() = runTest{
        val projects = listOf(
            createProject(name = "name1", states = listOf(createState(name = "In Progress"))),
        )
        coEvery { getAllProjectsUseCase() } returns projects
        coEvery { reader.readInt() } returns 1
        coEvery { reader.readString() } returns "In Progress"
        coEvery { addStateToProjectUseCase(any(), any()) } throws DuplicateStateException("In Progress")

        addStateToProjectUI.launchUi()

        coVerify { printer.displayLn("Error: State 'In Progress' already exists in project") }
    }

    @Test
    fun `should print error when unexpected exception is thrown`() = runTest{
        val projects = listOf(
            createProject(name = "name1", states = listOf(createState(name = "In Progress"))),
        )
        coEvery { getAllProjectsUseCase() } returns projects
        coEvery { reader.readInt() } returns 1
        coEvery { reader.readString() } returns "In Progress"
        coEvery { addStateToProjectUseCase(any(), any()) } throws RuntimeException("Unexpected error")

        addStateToProjectUI.launchUi()

        coVerify { printer.displayLn("Error: Unexpected error") }
    }

    @Test
    fun `should prompt again when input is null or blank`() = runTest {
        val projects = listOf(
            createProject(name = "project", states = emptyList())
        )
        coEvery { getAllProjectsUseCase() } returns projects
        coEvery { reader.readInt() } returnsMany listOf(
            null, null, null, 1, // read project #
        )
        coEvery { reader.readString() } returnsMany listOf(
            null, "newStateName", // new name input retries
        )

        addStateToProjectUI.launchUi()

        coVerify(exactly = 4) { printer.displayLn("\nInput cannot be empty.") }
        coVerify {
            addStateToProjectUseCase(
                projects[0].id.toString(),
                any()
            )
        }
    }

    @Test
    fun `should not find project when input is out of projects range`() = runTest {
        val projects = listOf(
            createProject(name = "project", states = emptyList())
        )
        coEvery { getAllProjectsUseCase() } returns projects
        coEvery { reader.readInt() } returns 5

        addStateToProjectUI.launchUi()

        coVerify { printer.displayLn("\nProject not found") }
    }
}