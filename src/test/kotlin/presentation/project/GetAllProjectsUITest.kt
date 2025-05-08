package presentation.project

import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import logic.model.Project
import logic.project.GetAllProjectsUseCase
import presentation.io.InputReader
import presentation.io.Printer
import java.util.*
import kotlin.test.Test

class GetAllProjectsUITest {
    private lateinit var inputReader: InputReader
    private lateinit var printer: Printer
    private lateinit var getAllProjectsUseCase: GetAllProjectsUseCase
    private lateinit var getAllProjectsUI: GetAllProjectsUI

    @BeforeEach
    fun setup() {
        inputReader = mockk()
        printer = mockk(relaxed = true)
        getAllProjectsUseCase = mockk()
        getAllProjectsUI = GetAllProjectsUI(printer, getAllProjectsUseCase)
    }

    @Test
    fun `should print all projects`() = runTest{
        // Given
        val projects = listOf(
            Project(id =UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), projectName = "Project One", states = emptyList()),
            Project(id =UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), projectName = "Project Two", states = emptyList())
        )
        coEvery { getAllProjectsUseCase.invoke() } returns projects

        // When
        getAllProjectsUI.launchUi()

        // Then
        coVerify { printer.displayLn("All Created Projects:") }
        coVerify { printer.displayLn("1) Project One") }
        coVerify { printer.displayLn("2) Project Two") }
    }

    @Test
    fun `should print error message when exception occurs`() = runTest {
        // Given
        coEvery { getAllProjectsUseCase.invoke() } throws RuntimeException("Something went wrong")

        // When
        getAllProjectsUI.launchUi()

        // Then
        coVerify { printer.displayLn("Error retrieving projects: Something went wrong") }
    }
}