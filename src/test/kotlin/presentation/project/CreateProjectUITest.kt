package presentation.project

import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import squad.abudhabi.logic.exceptions.ProjectNotFoundException
import logic.project.CreateProjectUseCase
import presentation.ui_io.InputReader
import presentation.ui_io.Printer

class CreateProjectUITest{
    private lateinit var createProjectUseCase: CreateProjectUseCase
    private lateinit var inputReader: InputReader
    private lateinit var printer: Printer
    private lateinit var createProjectUI: CreateProjectUI

    @BeforeEach
    fun setup() {
        createProjectUseCase = mockk(relaxed = true)
        inputReader = mockk()
        printer = mockk(relaxed = true)
        createProjectUI = CreateProjectUI(createProjectUseCase, inputReader, printer)
    }

    @Test
    fun `should print error when project name is null`() {
        every { inputReader.readString() } returns null

        createProjectUI.launchUi()

        verify { printer.displayLn("Project name cannot be empty.") }
        verify(exactly = 0) { createProjectUseCase(any(), any()) }
    }

    @Test
    fun `should print error when project name is blank`() {
        every { inputReader.readString() } returns "  "

        createProjectUI.launchUi()

        verify { printer.displayLn("Project name cannot be empty.") }
        verify(exactly = 0) { createProjectUseCase(any(), any()) }
    }

    @Test
    fun `should print error when number of states is invalid`() {
        every { inputReader.readString() } returns "My Project"
        every { inputReader.readInt() } returns null // simulate invalid input

        createProjectUI.launchUi()

        verify { printer.displayLn("Invalid number of states.") }
        verify(exactly = 0) { createProjectUseCase(any(), any()) }
    }

    @Test
    fun `should print error when number of states is negative`() {
        every { inputReader.readString() } returns "My Project"
        every { inputReader.readInt() } returns -2

        createProjectUI.launchUi()

        verify { printer.displayLn("Invalid number of states.") }
        verify(exactly = 0) { createProjectUseCase(any(), any()) }
    }

    @Test
    fun `should print error when state name is blank`() {
        every { inputReader.readString() } returnsMany listOf("My Project", "   ") // project name, then blank state
        every { inputReader.readInt() } returns 1

        createProjectUI.launchUi()

        verify { printer.displayLn("State name cannot be empty.") }
    }

    @Test
    fun `should call use case and print success when input is valid`() {
        every { inputReader.readString() } returnsMany listOf("My Project", "To Do", "In Progress")
        every { inputReader.readInt() } returns 2

        every { createProjectUseCase(any(), any()) } just Runs

        createProjectUI.launchUi()

        verify {
            createProjectUseCase("My Project", match { it.map { s -> s.name } == listOf("To Do", "In Progress") })
        }
        verify { printer.displayLn("Project 'My Project' created with 2 state(s).") }
    }

    @Test
    fun `should print error when exception is thrown`() {
        every { inputReader.readString() } returnsMany listOf("My Project", "Design")
        every { inputReader.readInt() } returns 1

        every { createProjectUseCase(any(), any()) } throws ProjectNotFoundException()

        createProjectUI.launchUi()

        verify { printer.displayLn("Error: Project Not Found") }
    }

    @Test
    fun `should print error when state name is null`() {
        every { inputReader.readString() } returnsMany listOf("My Project", null) // project name, then null
        every { inputReader.readInt() } returns 1

        createProjectUI.launchUi()

        verify { printer.displayLn("State name cannot be empty.") }
    }

    @Test
    fun `should allow project creation with zero states`() {
        every { inputReader.readString() } returns "Empty Project"
        every { inputReader.readInt() } returns 0
        every { createProjectUseCase(any(), any()) } just Runs

        createProjectUI.launchUi()

        verify { createProjectUseCase("Empty Project", emptyList()) }
        verify { printer.displayLn("Project 'Empty Project' created with 0 state(s).") }
    }

}