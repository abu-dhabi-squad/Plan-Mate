package presentation.project

import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import logic.exceptions.DuplicateStateException
import logic.exceptions.ProjectNotFoundException
import logic.model.State
import logic.project.AddStateToProjectUseCase
import presentation.ui_io.InputReader
import presentation.ui_io.Printer

class AddStateToProjectUITest{
    private lateinit var addStateToProjectUI: AddStateToProjectUI
    private lateinit var inputReader: InputReader
    private lateinit var printer: Printer
    private lateinit var addStateToProjectUseCase: AddStateToProjectUseCase

    @BeforeEach
    fun setUp() {
        addStateToProjectUseCase = mockk()
        inputReader = mockk()
        printer = mockk(relaxed = true)
        addStateToProjectUI = AddStateToProjectUI(addStateToProjectUseCase, inputReader, printer)
    }

    @Test
    fun `should print error when project ID is blank`() = runTest{
        coEvery { inputReader.readString() } returns ""

        addStateToProjectUI.launchUi()

        coVerify { printer.display("Enter the project ID to add a state to: ") }
        coVerify { printer.displayLn("Project ID cannot be empty.") }
        coVerify(exactly = 0) { addStateToProjectUseCase(any(), any()) }
    }

    @Test
    fun `should print error when state name is blank`() = runTest{
        coEvery { inputReader.readString() } returns "project123" andThen ""

        addStateToProjectUI.launchUi()

        coVerify { printer.display("Enter the project ID to add a state to: ") }
        coVerify { printer.display("Enter the new state name: ") }
        coVerify { printer.displayLn("State name cannot be empty.") }
        coVerify(exactly = 0) { addStateToProjectUseCase(any(), any()) }
    }


    @Test
    fun `should call use case and print success when inputs are valid`() = runTest{
        every { inputReader.readString() } returns "project123" andThen "In Progress"

        val state = State(name = "In Progress")
        coEvery { addStateToProjectUseCase("project123", match { it.name == "In Progress" }) } just Runs

        addStateToProjectUI.launchUi()

        coVerify { addStateToProjectUseCase("project123", match { it.name == "In Progress" }) }
        coVerify { printer.displayLn("State \"In Progress\" added to project \"project123\" successfully.") }
    }

    @Test
    fun `should print error when project not found exception is thrown`() = runTest{
        coEvery { inputReader.readString() } returns "project123" andThen "In Progress"
        coEvery { addStateToProjectUseCase(any(), any()) } throws ProjectNotFoundException()

        addStateToProjectUI.launchUi()

        coVerify { printer.displayLn("Error: Project Not Found") }  // Ensure this matches the exact exception message
    }

    @Test
    fun `should print error when duplicate state exception is thrown`() = runTest{
        coEvery { inputReader.readString() } returns "project123" andThen "In Progress"
        coEvery { addStateToProjectUseCase(any(), any()) } throws DuplicateStateException("In Progress")

        addStateToProjectUI.launchUi()

        // Update the expected message to match exactly what is printed
        coVerify { printer.displayLn("Error: State 'In Progress' already exists in project") }
    }

    @Test
    fun `should print error when unexpected exception is thrown`() = runTest{
        coEvery { inputReader.readString() } returns "project123" andThen "In Progress"
        coEvery { addStateToProjectUseCase(any(), any()) } throws RuntimeException("Unexpected error")

        addStateToProjectUI.launchUi()

        coVerify { printer.displayLn("Error: Unexpected error") }
    }

    @Test
    fun `should print error when projectId is null`() = runTest{
        coEvery { inputReader.readString() } returns null andThen "In Progress"  // projectId is null

        addStateToProjectUI.launchUi()

        coVerify { printer.display("Enter the project ID to add a state to: ") }
        coVerify { printer.displayLn("Project ID cannot be empty.") }
        coVerify(exactly = 0) { addStateToProjectUseCase(any(), any()) }
    }

    @Test
    fun `should print error when stateName is null`() = runTest{
        coEvery { inputReader.readString() } returns "project123" andThen null  // stateName is null

        addStateToProjectUI.launchUi()

        coVerify { printer.display("Enter the project ID to add a state to: ") }
        coVerify { printer.display("Enter the new state name: ") }
        coVerify { printer.displayLn("State name cannot be empty.") }
        coVerify(exactly = 0) { addStateToProjectUseCase(any(), any()) }
    }
}