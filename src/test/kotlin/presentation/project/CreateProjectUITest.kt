package presentation.project

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.audit.CreateAuditUseCase
import logic.project.CreateProjectUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.io.InputReader
import presentation.io.Printer
import logic.exceptions.ProjectNotFoundException
import logic.user.GetLoggedUserUseCase


class CreateProjectUITest{
    private lateinit var createProjectUseCase: CreateProjectUseCase
    private lateinit var inputReader: InputReader
    private lateinit var printer: Printer
    private lateinit var createProjectUI: CreateProjectUI
    private lateinit var createAuditUseCase: CreateAuditUseCase
    private lateinit var getLoggedUserUseCase: GetLoggedUserUseCase


    @BeforeEach
    fun setup() {
        createProjectUseCase = mockk(relaxed = true)
        createAuditUseCase = mockk(relaxed = true)
        getLoggedUserUseCase = mockk(relaxed = true)
        inputReader = mockk()
        printer = mockk(relaxed = true)
        createProjectUI = CreateProjectUI(createProjectUseCase, inputReader, printer, createAuditUseCase, getLoggedUserUseCase)
    }

    @Test
    fun `should print error when project name is null`() = runTest{
        coEvery { inputReader.readString() } returns null

        createProjectUI.launchUi()

        coVerify { printer.displayLn("\nProject name cannot be empty.") }
        coVerify(exactly = 0) { createProjectUseCase(any()) }
    }

    @Test
    fun `should print error when project name is blank`() = runTest{
        coEvery { inputReader.readString() } returns "  "

        createProjectUI.launchUi()

        coVerify { printer.displayLn("\nProject name cannot be empty.") }
        coVerify(exactly = 0) { createProjectUseCase(any()) }
    }

    @Test
    fun `should print error when number of states is invalid`() = runTest{
        coEvery { inputReader.readString() } returns "My Project"
        coEvery { inputReader.readInt() } returns null // simulate invalid input

        createProjectUI.launchUi()

        coVerify { printer.displayLn("\nInvalid number of states.") }
        coVerify(exactly = 0) { createProjectUseCase(any()) }
    }

    @Test
    fun `should print error when number of states is negative`() = runTest{
        coEvery { inputReader.readString() } returns "My Project"
        coEvery { inputReader.readInt() } returns -2

        createProjectUI.launchUi()

        coVerify { printer.displayLn("\nInvalid number of states.") }
        coVerify(exactly = 0) { createProjectUseCase(any()) }
    }

    @Test
    fun `should print error when state name is blank`() = runTest{
        coEvery { inputReader.readString() } returnsMany listOf("My Project", "   ") // project name, then blank state
        coEvery { inputReader.readInt() } returns 1

        createProjectUI.launchUi()

        coVerify { printer.displayLn("\nState name cannot be empty.") }
    }

    @Test
    fun `should call use case and print success when input is valid`() = runTest{
        coEvery { inputReader.readString() } returnsMany listOf("My Project", "To Do", "In Progress")
        coEvery { inputReader.readInt() } returns 2

        createProjectUI.launchUi()

        coVerify {
            createProjectUseCase(any())
        }
        coVerify { printer.displayLn("\nProject 'My Project' created with 2 state(s).") }
    }

    @Test
    fun `should print error when exception is thrown`() = runTest{
        coEvery { inputReader.readString() } returnsMany listOf("My Project", "Design")
        coEvery { inputReader.readInt() } returns 1

        coEvery { createProjectUseCase(any()) } throws ProjectNotFoundException()

        createProjectUI.launchUi()

        coVerify { printer.displayLn("\nError: Project Not Found") }
    }

    @Test
    fun `should print error when state name is null`() = runTest{
        coEvery { inputReader.readString() } returnsMany listOf("My Project", null) // project name, then null
        coEvery { inputReader.readInt() } returns 1

        createProjectUI.launchUi()

        coVerify { printer.displayLn("\nState name cannot be empty.") }
    }

    @Test
    fun `should allow project creation with zero states`() = runTest{
        coEvery { inputReader.readString() } returns "Empty Project"
        coEvery { inputReader.readInt() } returns 0

        createProjectUI.launchUi()

        coVerify { createProjectUseCase(any()) }
        coVerify { printer.displayLn("\nProject 'Empty Project' created with 0 state(s).") }
    }

    @Test
    fun `should throw exception when create audit throw exception`() = runTest{

        coEvery { inputReader.readString() } returnsMany listOf("My Project", "To Do", "In Progress")
        coEvery { inputReader.readInt() } returns 2
        coEvery { createAuditUseCase(any()) } throws Exception()

        createProjectUI.launchUi()

        coVerify { printer.displayLn("\nError: ${Exception().message}") }
    }
}