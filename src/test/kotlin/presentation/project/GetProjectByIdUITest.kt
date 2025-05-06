package presentation.project

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import squad.abudhabi.logic.model.Project
import logic.project.GetProjectByIdUseCase
import presentation.ui_io.InputReader
import presentation.ui_io.Printer
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
    fun `should print project name when project is found`() = runTest{
        // Given
        val projectId = "p1"
        val project = Project(id = projectId, projectName = "Test Project", states = emptyList())

        every { inputReader.readString() } returns projectId
        every { getProjectByIdUseCase(projectId) } returns project

        // When
        ui.launchUi()

        // Then
        verify { printer.displayLn("Enter project ID:") }
        verify { printer.displayLn("Project found: Test Project") }
    }

    @Test
    fun `should print error message when exception is thrown`() = runTest{
        // Given
        val projectId = "p1"
        val errorMessage = "Project not found"

        every { inputReader.readString() } returns projectId
        every { getProjectByIdUseCase(projectId) } throws Exception(errorMessage)

        // When
        ui.launchUi()

        // Then
        verify { printer.displayLn("Enter project ID:") }
        verify { printer.displayLn("Error: $errorMessage") }
    }

    @Test
    fun `should not call use case when user input is null or empty`() = runTest{
        // Given
        every { inputReader.readString() } returns null

        // When
        ui.launchUi()

        // Then
        verify(exactly = 0) { getProjectByIdUseCase(any()) }
        verify { printer.displayLn("Enter project ID:") }
        verify { printer.displayLn("Project ID cannot be empty.") }
    }

    @Test
    fun `should not call use case when user input is empty`() = runTest{
        // Given
        every { inputReader.readString() } returns ""

        // When
        ui.launchUi()

        // Then
        verify(exactly = 0) { getProjectByIdUseCase(any()) }
        verify { printer.displayLn("Enter project ID:") }
        verify { printer.displayLn("Project ID cannot be empty.") }
    }
}