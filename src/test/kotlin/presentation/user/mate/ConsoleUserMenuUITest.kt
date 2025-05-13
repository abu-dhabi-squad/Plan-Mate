package presentation.user.mate

import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import presentation.UIFeature
import presentation.UiLauncher
import presentation.io.Printer
import presentation.user.mate.ConsoleUserMenuUI
import presentation.utils.PromptService

class ConsoleUserMenuUITest {

    private lateinit var printer: Printer
    private lateinit var promptService: PromptService
    private lateinit var uiLauncher: UiLauncher
    private lateinit var view: ConsoleUserMenuUI

    @BeforeEach
    fun setup() {
        printer = mockk(relaxed = true)
        promptService = mockk(relaxed = true)
        uiLauncher = mockk(relaxed = true)

        view = ConsoleUserMenuUI(
            listOf(
                UIFeature("View Profile", 1, uiLauncher),
                UIFeature("View Reports", 2, uiLauncher),
                UIFeature("Chat with Coach", 3, uiLauncher),
            ),
            printer,
            promptService
        )
    }

    @Test
    fun `launchUi should display welcome message and feature labels`() = runTest {
        //Given
        coEvery { promptService.promptNonEmptyInt(any()) } returns 1 andThenThrows RuntimeException()
        coEvery { uiLauncher.launchUi() } just Runs

        //When
        assertThrows<RuntimeException> {
            view.launchUi()
        }

        //Then
        verify { printer.displayLn(match { it.toString().contains("Welcome to PlanMate App") }) }
        verify { printer.displayLn(match { it.toString().contains("View Profile") }) }
        verify { printer.displayLn(match { it.toString().contains("View Reports") }) }
        verify { printer.displayLn(match { it.toString().contains("Chat with Coach") }) }
    }

    @Test
    fun `launchUi should call feature ui when valid input`() = runTest {
        //Given
        coEvery { promptService.promptNonEmptyInt(any()) } returns 1 andThenThrows RuntimeException()
        coEvery { uiLauncher.launchUi() } just Runs

        //When
        assertThrows<RuntimeException> {
            view.launchUi()
        }

        //Then
        coVerify { uiLauncher.launchUi() }
    }

    @Test
    fun `launchUi should show invalid input message when input is out of range`() = runTest {
        //Given
        coEvery { promptService.promptNonEmptyInt(any()) } returns 999 andThenThrows RuntimeException()

        //When
        assertThrows<RuntimeException> {
            view.launchUi()
        }

        //Then
        verify { printer.displayLn(match { it.toString().contains("Invalid input") }) }
    }

    @Test
    fun `launchUi should loop when input is invalid then valid`() = runTest {
        //Given
        coEvery { promptService.promptNonEmptyInt(any()) } returnsMany listOf(999, 2) andThenThrows RuntimeException()
        coEvery { uiLauncher.launchUi() } just Runs

        //When
        assertThrows<RuntimeException> {
            view.launchUi()
        }

        //Then
        verify { printer.displayLn(match { it.toString().contains("Invalid input") }) }
        coVerify { uiLauncher.launchUi() }
    }

    @Test
    fun `should not crash if no feature matches input id`() = runTest {
        // Given
        val unmatchedId = 5
        coEvery { promptService.promptNonEmptyInt(any()) } returns unmatchedId andThenThrows RuntimeException()

        // When & Then
        assertThrows<RuntimeException> {
            view.launchUi()
        }

        coVerify(exactly = 0) { uiLauncher.launchUi() }
    }

    @Test
    fun `should handle case when no matching feature is found`() = runTest {
        // Given
        coEvery { promptService.promptNonEmptyInt(any()) } returns 10 andThenThrows RuntimeException()

        // When & Then
        assertThrows<RuntimeException> {
            view.launchUi()
        }

        coVerify(exactly = 0) { uiLauncher.launchUi() }
    }

    @Test
    fun `should call presentFeature multiple times`() = runTest {
        //Given
        coEvery { promptService.promptNonEmptyInt(any()) } returnsMany listOf(999, 1) andThenThrows RuntimeException()
        coEvery { uiLauncher.launchUi() } just Runs

        //When
        assertThrows<RuntimeException> {
            view.launchUi()
        }

        //Then
        coVerify(exactly = 1) { uiLauncher.launchUi() }
        verify(atLeast = 2) { printer.displayLn(any()) } // called multiple times from looping
    }
}
