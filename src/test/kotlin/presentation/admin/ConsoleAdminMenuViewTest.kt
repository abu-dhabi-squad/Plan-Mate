package presentation.admin

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.UIFeature
import presentation.UiLauncher
import presentation.io.InputReader
import presentation.io.Printer

class ConsoleAdminMenuViewTest {

    private lateinit var printer: Printer
    private lateinit var reader: InputReader
    private lateinit var uiLauncher: UiLauncher
    private lateinit var view: ConsoleAdminMenuView

    @BeforeEach
    fun setup() {
        printer = mockk(relaxed = true)
        reader = mockk(relaxed = true)
        uiLauncher = mockk(relaxed = true)

        val features = listOf(
            UIFeature("Create Project", 1, uiLauncher),
            UIFeature("Edit Project", 2, uiLauncher),
            UIFeature("Delete Project", 3, uiLauncher),
        )

        // Simulate one valid input, then force exit by throwing instead of looping forever
        every { reader.readInt() } returns 1 andThenThrows RuntimeException("stop loop")

        view = ConsoleAdminMenuView(features, printer, reader)
    }

    @Test
    fun `launchUi should print expected feature labels and welcome message`() = runTest{
        try {
            view.launchUi()
        } catch (_: RuntimeException) {
            // Expected to stop infinite recursion from presentFeature()
        }

        verify { printer.displayLn(match { it.toString().contains("Welcome to PlanMate Admin Dashboard") }) }

         verify { printer.displayLn(match { it.toString().contains("Create Project") }) }
        verify { printer.displayLn(match { it.toString().contains("Edit Project") }) }
        verify { printer.displayLn(match { it.toString().contains("Delete Project") }) }

    }

    @Test
    fun `launchUi should print Invalid input when input is not in range`() = runTest{
        // Simulate invalid input first, then break the loop
        every { reader.readInt() } returns 999 andThenThrows RuntimeException("stop loop")

        try {
            view.launchUi()
        } catch (_: RuntimeException) {
            // Stop infinite recursion
        }

        verify { printer.displayLn(match { it.toString().contains("Invalid input") }) }
    }
}
