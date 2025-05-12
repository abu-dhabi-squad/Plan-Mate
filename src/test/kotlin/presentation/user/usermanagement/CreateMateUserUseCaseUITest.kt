package presentation.user.usermanagement

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import logic.authentication.CreateMateUserUseCase
import logic.exceptions.EmptyUsernameException
import logic.exceptions.UserAlreadyExistsException
import logic.model.UserType
import presentation.io.Printer
import presentation.presentation.user.usermanagement.CreateMateUserUseCaseUI
import presentation.presentation.utils.PromptService
import kotlin.test.Test


class CreateMateUserUseCaseUITest {

    private lateinit var useCase: CreateMateUserUseCase
    private lateinit var promptService: PromptService
    private lateinit var printer: Printer
    private lateinit var createMateUserUI: CreateMateUserUseCaseUI

    @BeforeEach
    fun setup() {
        useCase = mockk(relaxed = true)
        promptService = mockk(relaxed = true)
        printer = mockk(relaxed = true)

        createMateUserUI = CreateMateUserUseCaseUI(useCase, printer, promptService)
    }

    @Test
    fun `should create user successfully`() = runTest {
        // Given
        every { promptService.promptNonEmptyString(any()) } returnsMany listOf("shahd", "123456")

        // When:
        createMateUserUI.launchUi()

        // Then:
        coVerify {
            useCase.invoke(
                match {
                    it.username == "shahd" && it.password == "123456" && it.userType == UserType.MATE
                }
            )
        }
        // Then:
        verify { printer.displayLn("User created successfully!") }
    }

    @Test
    fun `should throw exception when username is empty`() = runTest {
        // Given:
        every { promptService.promptNonEmptyString(any()) } returnsMany listOf("", "shahd", "123456789")
        coEvery { useCase.invoke(any()) } throws EmptyUsernameException()

        // When:
        createMateUserUI.launchUi()

        // Then:
        verify { printer.displayLn("Input cannot be empty.") }
    }

    @Test
    fun `should show error when password is empty`() = runTest {
        // Given:
        every { promptService.promptNonEmptyString(any()) } returnsMany listOf("shahd", "", "123456")

        // When:
        createMateUserUI.launchUi()

        // Then:
        verify { printer.displayLn("Input cannot be empty.") }
    }

    @Test
    fun `should show error when user already exists`() = runTest {
        val name = "shahd"
        // Given:
        every { promptService.promptNonEmptyString(any()) } returnsMany listOf(name, "123456")
        coEvery { useCase.invoke(any()) } throws UserAlreadyExistsException(name)

        // When:
        createMateUserUI.launchUi()

        // Then
        verify { printer.displayLn("Username '$name' already exists") }
    }
}
