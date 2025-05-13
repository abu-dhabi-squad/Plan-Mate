package presentation.user.admin

import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import presentation.UIFeature
import presentation.UiLauncher
import presentation.io.Printer
import presentation.presentation.user.admin.ConsoleAdminMenuUI
import presentation.presentation.utils.PromptService

class ConsoleAdminMenuUITest {

    private lateinit var printer: Printer
    private lateinit var promptService: PromptService
    private lateinit var uiLauncher: UiLauncher
    private lateinit var view: ConsoleAdminMenuUI


    @BeforeEach
    fun setup() {
            printer = mockk(relaxed = true)
            promptService = mockk(relaxed = true)
            uiLauncher = mockk(relaxed = true)

            view = ConsoleAdminMenuUI(
                listOf(
                    UIFeature("Create Project", 1, uiLauncher),
                    UIFeature("Edit Project", 2, uiLauncher),
                    UIFeature("Delete Project", 3, uiLauncher),
                ),
                printer,
                promptService
            )


    }

    @Test
    fun `launchUi should print welcome message and feature labels`() = runTest {
        // Arrange
        coEvery { promptService.promptNonEmptyInt(any()) } returns 1 andThenThrows RuntimeException()
        coEvery { uiLauncher.launchUi() } just Runs

        // Act
        assertThrows<RuntimeException> {
            view.launchUi()
        }

        // Assert
        verify { printer.displayLn(match { it.toString().contains("Welcome to PlanMate Admin Dashboard") }) }
        verify { printer.displayLn(match { it.toString().contains("Create Project") }) }
        verify { printer.displayLn(match { it.toString().contains("Edit Project") }) }
        verify { printer.displayLn(match { it.toString().contains("Delete Project") }) }
    }

    @Test
    fun `launchUi should print Invalid input when input is out of range`() = runTest {
        // Arrange
        coEvery { promptService.promptNonEmptyInt(any()) } returns 999 andThenThrows RuntimeException()

        // Act
        assertThrows<RuntimeException> {
            view.launchUi()
        }

        // Assert
        verify { printer.displayLn(match { it.toString().contains("Invalid input") }) }
    }

    @Test
    fun `launchUi should handle exception thrown by uiLauncher`() = runTest {
        coEvery { promptService.promptNonEmptyInt(any()) } returns 1 andThenThrows RuntimeException()
        coEvery { uiLauncher.launchUi() } throws RuntimeException()

        assertThrows<RuntimeException> {
            view.launchUi()
        }

        coVerify { uiLauncher.launchUi() }
    }
    @Test
    fun `launchUi should loop back to presentFeature`() = runTest {
        coEvery { promptService.promptNonEmptyInt(any()) } returnsMany listOf(999, 1) andThenThrows RuntimeException()
        coEvery { uiLauncher.launchUi() } just Runs

        assertThrows<RuntimeException> {
            view.launchUi()
        }

        verify { printer.displayLn(match { it.toString().contains("Invalid input") }) }
        coVerify { uiLauncher.launchUi() }
    }





}
