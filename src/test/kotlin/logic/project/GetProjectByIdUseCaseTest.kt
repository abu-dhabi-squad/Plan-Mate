package logic.project

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import squad.abudhabi.logic.exceptions.ProjectNotFoundException
import squad.abudhabi.logic.model.Project
import squad.abudhabi.logic.project.GetProjectByIdUseCase
import squad.abudhabi.presentation.project.GetProjectByIdUI
import squad.abudhabi.presentation.ui_io.InputReader
import squad.abudhabi.presentation.ui_io.Printer
import kotlin.test.Test

class GetProjectByIdUITest {
    private lateinit var inputReader: InputReader
    private lateinit var printer: Printer
    private lateinit var getProjectByIdUseCase: GetProjectByIdUseCase
    private lateinit var getProjectByIdUI: GetProjectByIdUI

    @BeforeEach
    fun setup() {
        inputReader = mockk()
        printer = mockk(relaxed = true)
        getProjectByIdUseCase = mockk()
        getProjectByIdUI = GetProjectByIdUI(inputReader, printer, getProjectByIdUseCase)
    }

    @Test
    fun `should print error when input is empty`() {
        every { inputReader.readString() } returns "   "

        getProjectByIdUI.launchUi()

        verify { printer.displayLn("Project ID cannot be empty.") }
    }

    @Test
    fun `should print project name when project is found`() {
        val project = Project(id = "1", projectName = "Test Project", states = emptyList())
        every { inputReader.readString() } returns "1"
        every { getProjectByIdUseCase("1") } returns project

        getProjectByIdUI.launchUi()

        verify { printer.displayLn("Project found: Test Project") }
    }

    @Test
    fun `should print error when project not found`() {
        every { inputReader.readString() } returns "99"
        every { getProjectByIdUseCase("99") } throws ProjectNotFoundException()

        getProjectByIdUI.launchUi()

        verify { printer.displayLn("Error: Project Not Found") }
    }

    @Test
    fun `should print error when input is null`() {
        every { inputReader.readString() } returns null

        getProjectByIdUI.launchUi()

        verify { printer.displayLn("Project ID cannot be empty.") }
    }
}