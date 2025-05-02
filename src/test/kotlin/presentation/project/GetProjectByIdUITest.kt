package presentation.project

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
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
    private lateinit var ui: GetProjectByIdUI

    @BeforeEach
    fun setup() {
        inputReader = mockk()
        printer = mockk(relaxed = true)
        getProjectByIdUseCase = mockk()
        ui = GetProjectByIdUI(inputReader, printer, getProjectByIdUseCase)
    }

    @Test
    fun `should print project name when project is found`() {
        // Given
        val projectId = "p1"
        val project = Project(id = projectId, projectName = "Test Project", states = emptyList())

        every { inputReader.readString() } returns projectId
        every { getProjectByIdUseCase(projectId) } returns project

        // When
        ui.launchUi()

        // Then
        verify { printer.displayLn("Enter project id : ") }
        verify { printer.displayLn("Test Project") }
    }

    @Test
    fun `should print error message when exception is thrown`() {
        // Given
        val projectId = "p1"
        val errorMessage = "Project not found"

        every { inputReader.readString() } returns projectId
        every { getProjectByIdUseCase(projectId) } throws Exception(errorMessage)

        // When
        ui.launchUi()

        // Then
        verify { printer.displayLn("Enter project id : ") }
        verify { printer.displayLn(errorMessage) }
    }

    @Test
    fun `should not call use case when user input is null`() {
        // Given
        every { inputReader.readString() } returns null

        // When
        ui.launchUi()

        // Then
        verify(exactly = 0) { getProjectByIdUseCase(any()) }
        verify { printer.displayLn("Enter project id : ") }
    }
}