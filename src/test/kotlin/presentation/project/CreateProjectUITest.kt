package presentation.project

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import squad.abudhabi.logic.model.State
import squad.abudhabi.logic.project.CreateProjectUseCase
import squad.abudhabi.presentation.project.CreateProjectUI
import squad.abudhabi.presentation.ui_io.InputReader
import squad.abudhabi.presentation.ui_io.Printer

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
    fun `launchUi should create project with empty state list when user enters 0 states`() {
        every { inputReader.readString() } returns "My Project"
        every { inputReader.readInt() } returns 0

        createProjectUI.launchUi()

        verify { createProjectUseCase("My Project", emptyList()) }
        verify { printer.displayLn("Project 'My Project' created with 0 state(s).") }
    }

    @Test
    fun `launchUi should create project with one state`() {
        every { inputReader.readString() } returnsMany listOf("My Project", "To Do")
        every { inputReader.readInt() } returns 1

        createProjectUI.launchUi()

        val stateSlot = slot<List<State>>()
        verify { createProjectUseCase("My Project", capture(stateSlot)) }

        assertThat(stateSlot.captured).hasSize(1)
        assertThat(stateSlot.captured[0].name).isEqualTo("To Do")
    }

    @Test
    fun `launchUi should not create project if project name is blank`() {
        every { inputReader.readString() } returns "   "

        createProjectUI.launchUi()

        verify { printer.displayLn("Project name cannot be empty.") }
        verify(exactly = 0) { createProjectUseCase(any(), any()) }
    }

    @Test
    fun `launchUi should not create project if number of states is invalid`() {
        every { inputReader.readString() } returns "My Project"
        every { inputReader.readInt() } returns -2

        createProjectUI.launchUi()

        verify { printer.displayLn("Invalid number of states.") }
        verify(exactly = 0) { createProjectUseCase(any(), any()) }
    }

    @Test
    fun `launchUi should not create project if a state name is empty`() {
        every { inputReader.readString() } returnsMany listOf("My Project", "   ")
        every { inputReader.readInt() } returns 1

        createProjectUI.launchUi()

        verify { printer.displayLn("State name cannot be empty.") }
        verify(exactly = 0) { createProjectUseCase(any(), any()) }
    }
}