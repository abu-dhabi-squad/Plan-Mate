package presentation.project

import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import logic.model.Project
import logic.project.GetProjectByIdUseCase
import presentation.ui_io.InputReader
import presentation.ui_io.Printer
import java.util.*
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
        val projectId =UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a")
        val project = Project(id = projectId, projectName = "Test Project", states = emptyList())

        coEvery { inputReader.readString() } returns projectId.toString()
        coEvery { getProjectByIdUseCase(projectId.toString()) } returns project

        // When
        ui.launchUi()

        // Then
        coVerify { printer.displayLn("Enter project ID:") }
        coVerify { printer.displayLn("Project found: Test Project") }
    }

    @Test
    fun `should print error message when exception is thrown`() = runTest{
        // Given
        val projectId = UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a")
        val errorMessage = "Project not found"

        coEvery { inputReader.readString() } returns projectId.toString()
        coEvery { getProjectByIdUseCase(projectId.toString()) } throws Exception(errorMessage)

        // When
        ui.launchUi()

        // Then
        coVerify { printer.displayLn("Enter project ID:") }
        coVerify { printer.displayLn("Error: $errorMessage") }
    }

    @Test
    fun `should not call use case when user input is null or empty`() = runTest{
        // Given
        coEvery { inputReader.readString() } returns null

        // When
        ui.launchUi()

        // Then
        coVerify(exactly = 0) { getProjectByIdUseCase(any()) }
        coVerify { printer.displayLn("Enter project ID:") }
        coVerify { printer.displayLn("Project ID cannot be empty.") }
    }

    @Test
    fun `should not call use case when user input is empty`() = runTest{
        // Given
        coEvery { inputReader.readString() } returns ""

        // When
        ui.launchUi()

        // Then
        coVerify(exactly = 0) { getProjectByIdUseCase(any()) }
        coVerify { printer.displayLn("Enter project ID:") }
        coVerify { printer.displayLn("Project ID cannot be empty.") }
    }
}