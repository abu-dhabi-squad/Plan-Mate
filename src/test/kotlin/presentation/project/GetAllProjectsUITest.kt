package presentation.project

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import squad.abudhabi.logic.model.Project
import squad.abudhabi.logic.project.GetAllProjectsUseCase
import squad.abudhabi.presentation.project.GetAllProjectsUI
import squad.abudhabi.presentation.ui_io.InputReader
import squad.abudhabi.presentation.ui_io.Printer
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
        getAllProjectsUI = GetAllProjectsUI(inputReader, printer, getAllProjectsUseCase)
    }

    @Test
    fun `should print all projects`() {
        // Given
        val projects = listOf(
            Project(id = "p1", projectName = "Project One", states = emptyList()),
            Project(id = "p2", projectName = "Project Two", states = emptyList())
        )
        every { getAllProjectsUseCase.invoke() } returns projects

        // When
        getAllProjectsUI.launchUi()

        // Then
        verify { printer.displayLn("all projects created : \n") }
        verify { printer.displayLn("1) Project One") }
        verify { printer.displayLn("2) Project Two") }
    }

    @Test
    fun `should print error message when exception occurs`() {
        // Given
        every { getAllProjectsUseCase.invoke() } throws RuntimeException("Something went wrong")

        // When
        getAllProjectsUI.launchUi()

        // Then
        verify { printer.displayLn("Something went wrong") }
    }
}