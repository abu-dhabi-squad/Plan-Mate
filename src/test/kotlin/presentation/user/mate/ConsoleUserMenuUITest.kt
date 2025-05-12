package presentation.user.mate

import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.UIFeature
import presentation.UiLauncher
import presentation.io.Printer
import presentation.presentation.user.mate.ConsoleUserMenuUI
import presentation.presentation.utils.PromptService

class ConsoleUserMenuUITest {
    private lateinit var printer: Printer
    private lateinit var promptService: PromptService
    private lateinit var uiLauncher: UiLauncher
    private lateinit var view: ConsoleUserMenuUI

    val features = listOf(
        UIFeature("Create Project", 1, uiLauncher),
        UIFeature("Edit Project", 2, uiLauncher),
        UIFeature("Delete Project", 3, uiLauncher),
    )

    @BeforeEach
    fun setup() {
        printer = mockk(relaxed = true)
        promptService = mockk(relaxed = true)
        uiLauncher = mockk(relaxed = true)

        view = ConsoleUserMenuUI(features, printer, promptService)
    }

    @Test
    fun `launchUi should print expected feature labels and welcome message`() = runTest{
        //Given
        coEvery { promptService.promptNonEmptyInt(any()) } returns 1 andThenThrows RuntimeException("stop loop")
        coEvery { features[0].uiLauncher.launchUi() } just Runs

        //When
        try {
            view.launchUi()
        } catch (_: RuntimeException) {
            // Expected to stop infinite recursion from presentFeature()
        }

        //Then
        verify { printer.displayLn(match { it.toString().contains("Welcome to PlanMate App") }) }
        verify { printer.displayLn(match { it.toString().contains("Create Project") }) }
        verify { printer.displayLn(match { it.toString().contains("Edit Project") }) }
        verify { printer.displayLn(match { it.toString().contains("Delete Project") }) }

    }

    @Test
    fun `launchUi should print Invalid input when input is not in range`() = runTest{
        // Simulate invalid input first, then break the loop
        // Given
        coEvery { promptService.promptNonEmptyInt(any()) } returns 999 andThenThrows RuntimeException("stop loop")

        //When
        try {
            view.launchUi()
        } catch (_: RuntimeException) {
            // Stop infinite recursion
        }

        //Then
        verify { printer.displayLn(match { it.toString().contains("Invalid input") }) }
    }
}