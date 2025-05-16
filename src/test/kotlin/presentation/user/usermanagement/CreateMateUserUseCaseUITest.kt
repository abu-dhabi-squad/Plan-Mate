package presentation.user.usermanagement

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import logic.authentication.CreateMateUserUseCase
import logic.exceptions.EmptyUsernameException
import logic.exceptions.UserAlreadyExistsException
import logic.model.User.UserType
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import presentation.io.Printer
import presentation.utils.PromptUtils

class CreateMateUserUseCaseUITest {

    private lateinit var useCase: CreateMateUserUseCase
    private lateinit var promptUtils: PromptUtils
    private lateinit var printer: Printer
    private lateinit var createMateUserUI: CreateMateUserUseCaseUI

    @BeforeEach
    fun setUp() {
        useCase = mockk(relaxed = true)
        promptUtils = mockk(relaxed = true)
        printer = mockk(relaxed = true)
        createMateUserUI = CreateMateUserUseCaseUI(useCase, printer, promptUtils)
    }

    @Test
    fun `should create user successfully`() = runTest {
        //Given
        val username = "shahd"
        val password = "123456"
        every { promptUtils.promptNonEmptyString(any()) } returnsMany listOf(username, password)

        //When
        createMateUserUI.launchUi()

        //Then
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
        //Given
        val username = "  "
        val password = "123456"
        every { promptUtils.promptNonEmptyString(any()) } returnsMany listOf(username, password)
        coEvery { useCase.invoke(any()) } throws EmptyUsernameException()

        //When
        createMateUserUI.launchUi()

        //Then
        verify { printer.displayLn("\n${EmptyUsernameException().message}") }
    }

    @Test
    fun `should display message when user already exists`() = runTest {
        //Given
        val username = "shahd"
        val password = "123456"
        every { promptUtils.promptNonEmptyString(any()) } returnsMany listOf(username, password)
        coEvery { useCase.invoke(any()) } throws UserAlreadyExistsException(username)

        //When
        createMateUserUI.launchUi()

        //Then
        verify { printer.displayLn("\nUsername '$username' already exists") }
    }

    @Test
    fun `should display generic error when unknown exception occurs`() = runTest {
        //Given
        val username = "shahd"
        val password = "123456"
        every { promptUtils.promptNonEmptyString(any()) } returnsMany listOf(username, password)
        coEvery { useCase.invoke(any()) } throws RuntimeException("Unexpected error")

        //When
        createMateUserUI.launchUi()

        //Then
        verify { printer.displayLn("\nUnexpected error") }
    }

    @Test
    fun `should display message for custom unexpected exception`() = runTest {
        // Given
        val username = "shahd"
        val password = "123456"
        every { promptUtils.promptNonEmptyString(any()) } returnsMany listOf(username, password)
        coEvery { useCase.invoke(any()) } throws Exception("Something weird happened")

        // When
        createMateUserUI.launchUi()

        // Then
        verify { printer.displayLn("\nSomething weird happened") }
    }
}
