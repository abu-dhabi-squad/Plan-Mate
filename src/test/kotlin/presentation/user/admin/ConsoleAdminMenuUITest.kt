package presentation.user.admin

import io.mockk.coVerify
import io.mockk.Runs
import io.mockk.just
import io.mockk.coEvery
import io.mockk.verify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import presentation.UIFeature
import presentation.UiLauncher
import presentation.io.Printer
import presentation.utils.PromptService
import kotlin.test.assertFailsWith

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
        // Given
        coEvery { promptService.promptNonEmptyInt(any()) } returns 1 andThenThrows RuntimeException()
        coEvery { uiLauncher.launchUi() } just Runs

        // When & Then
        assertThrows<RuntimeException> {
            view.launchUi()
        }
        verify { printer.displayLn(match { it.toString().contains("Welcome to PlanMate Admin Dashboard") }) }
        verify { printer.displayLn(match { it.toString().contains("Create Project") }) }
        verify { printer.displayLn(match { it.toString().contains("Edit Project") }) }
        verify { printer.displayLn(match { it.toString().contains("Delete Project") }) }
    }

    @Test
    fun `launchUi should print Invalid input when input is out of range`() = runTest {
        // Given
        coEvery { promptService.promptNonEmptyInt(any()) } returns 999 andThenThrows RuntimeException()

        // When & Then
        assertThrows<RuntimeException> {
            view.launchUi()
        }
        verify { printer.displayLn(match { it.toString().contains("Invalid input") }) }
    }

    @Test
    fun `launchUi should handle exception thrown by uiLauncher`() = runTest {
        // Given
        coEvery { promptService.promptNonEmptyInt(any()) } returns 1 andThenThrows RuntimeException()
        coEvery { uiLauncher.launchUi() } throws RuntimeException()

        // When & Then
        assertThrows<RuntimeException> {
            view.launchUi()
        }
        coVerify { uiLauncher.launchUi() }
    }

    @Test
    fun `launchUi should loop back to presentFeature`() = runTest {
        // Given
        coEvery { promptService.promptNonEmptyInt(any()) } returnsMany listOf(999, 1) andThenThrows RuntimeException()
        coEvery { uiLauncher.launchUi() } just Runs

        // When & Then
        assertThrows<RuntimeException> {
            view.launchUi()
        }
        verify { printer.displayLn(match { it.toString().contains("Invalid input") }) }
        coVerify { uiLauncher.launchUi() }
    }

    @Test
    fun `should exit the program when input is 0`() = runTest {
        //Given
        coEvery { promptService.promptNonEmptyInt(any()) } returns 0

        //When & Then
        assertFailsWith<IllegalStateException> {
            view.launchUi()
        }
    }

    @Test
    fun `should select last feature and launch`() = runTest {
        //Given
        val lastFeature = UIFeature("Last Feature", 3, uiLauncher)
        val features = listOf(
            UIFeature("Create Project", 1, mockk(relaxed = true)),
            UIFeature("Edit Project", 2, mockk(relaxed = true)),
            lastFeature
        )

        //When & Then
        view = ConsoleAdminMenuUI(features, printer, promptService)
        coEvery { promptService.promptNonEmptyInt(any()) } returns 3 andThenThrows RuntimeException()
        coEvery { lastFeature.uiLauncher.launchUi() } just Runs

        //Then
        assertThrows<RuntimeException> {
            view.launchUi()
        }

        coVerify { lastFeature.uiLauncher.launchUi() }
    }

    @Test
    fun `printFeatureLine should format line correctly`() {
        //Given
        val feature = UIFeature("Sample Feature", 7, mockk())
        view = ConsoleAdminMenuUI(listOf(feature), printer, promptService)

        //When
        runBlocking { view.launchUi() }

        //Then
        verify { printer.displayLn(match { it.toString().contains(" 7.  Sample Feature") }) }
    }

}
