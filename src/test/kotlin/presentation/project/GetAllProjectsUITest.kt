package presentation.project

import helper.createProject
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import logic.project.GetAllProjectsUseCase
import presentation.io.Printer
import presentation.utils.extensions.printWithStates
import kotlin.test.Test

class GetAllProjectsUITest {
    private val printer: Printer = mockk(relaxed = true)
    private val getAllProjectsUseCase: GetAllProjectsUseCase = mockk(relaxed = true)
    private lateinit var ui: GetAllProjectsUI

    @BeforeEach
    fun setup() {
        ui = GetAllProjectsUI(
            printer = printer,
            getAllProjectsUseCase = getAllProjectsUseCase
        )
    }

    @Test
    fun `getAllProjectsUseCase should print all projects when nothing went wrong`() = runTest{
        // Given
        val projects = listOf(
            createProject(name = "Project One"),
            createProject(name = "Project Two")
        )
        coEvery { getAllProjectsUseCase() } returns projects
        // When
        ui.launchUi()
        // Then
        coVerify { projects.printWithStates(printer) }
    }

    @Test
    fun `getAllProjectsUseCase should print error message when exception occurs`() = runTest {
        // Given
        coEvery { getAllProjectsUseCase.invoke() } throws Exception()
        // When
        ui.launchUi()
        // Then
        verify { printer.displayLn(match { it.toString().contains("${Exception().message}") }) }
    }
}