package presentation.user.usermanagement

import io.mockk.*
import kotlinx.coroutines.test.runTest
import logic.authentication.CreateMateUserUseCase
import logic.exceptions.EmptyUsernameException
import logic.exceptions.UserAlreadyExistsException
import logic.model.UserType
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import presentation.io.Printer
import presentation.presentation.user.usermanagement.CreateMateUserUseCaseUI
import presentation.presentation.utils.PromptService

class CreateMateUserUseCaseUITest {

    private lateinit var useCase: CreateMateUserUseCase
    private lateinit var promptService: PromptService
    private lateinit var printer: Printer
    private lateinit var createMateUserUI: CreateMateUserUseCaseUI

    @BeforeEach
    fun setUp() {
        useCase = mockk(relaxed = true)
        promptService = mockk(relaxed = true)
        printer = mockk(relaxed = true)
        createMateUserUI = CreateMateUserUseCaseUI(useCase, printer, promptService)
    }

    @Test
    fun `should create user successfully`() = runTest {
        val username = "shahd"
        val password = "123456"
        every { promptService.promptNonEmptyString(any()) } returnsMany listOf(username, password)

        createMateUserUI.launchUi()

        coVerify {
            useCase.invoke(
                match {
                    it.username == username &&
                            it.password == password &&
                            it.userType == UserType.MATE
                }
            )
        }

        verify { printer.displayLn("\nUser created successfully!") }
    }

    @Test
    fun `should display message when username is empty from useCase`() = runTest {
        val username = "  "
        val password = "123456"
        every { promptService.promptNonEmptyString(any()) } returnsMany listOf(username, password)
        coEvery { useCase.invoke(any()) } throws EmptyUsernameException()

        createMateUserUI.launchUi()

        verify { printer.displayLn("\n${EmptyUsernameException().message}") }
    }

    @Test
    fun `should display message when user already exists`() = runTest {
        val username = "shahd"
        val password = "123456"
        every { promptService.promptNonEmptyString(any()) } returnsMany listOf(username, password)
        coEvery { useCase.invoke(any()) } throws UserAlreadyExistsException(username)

        createMateUserUI.launchUi()

        verify { printer.displayLn("\nUsername '$username' already exists") }
    }

    @Test
    fun `should display generic error when unknown exception occurs`() = runTest {
        val username = "shahd"
        val password = "123456"
        every { promptService.promptNonEmptyString(any()) } returnsMany listOf(username, password)
        coEvery { useCase.invoke(any()) } throws RuntimeException("Unexpected error")

        createMateUserUI.launchUi()

        verify { printer.displayLn("\nUnexpected error") }
    }
}
