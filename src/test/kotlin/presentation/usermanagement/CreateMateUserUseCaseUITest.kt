package presentation.usermanagement

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
import presentation.io.InputReader
import presentation.io.Printer
import kotlin.test.Test


class CreateMateUserUseCaseUITest {

    private lateinit var useCase: CreateMateUserUseCase
    private lateinit var inputReader: InputReader
    private lateinit var printer: Printer
    private lateinit var createMateUserUI: CreateMateUserUseCaseUI

    @BeforeEach
    fun setup() {
        useCase = mockk(relaxed = true)
        inputReader = mockk(relaxed = true)
        printer = mockk(relaxed = true)
        createMateUserUI = CreateMateUserUseCaseUI(useCase, inputReader, printer)
    }

    @Test
    fun `should create user successfully`() = runTest{
        // Given
        every { inputReader.readString() } returnsMany listOf("shahd", "123456")

        // When:
        createMateUserUI.launchUi()

        // Then:
        coVerify {
            useCase.invoke(
                match {
                    it.username == "shahd" &&
                            it.password == "123456" &&
                            it.userType == UserType.MATE
                }
            )
        }
        // Then:
        verify { printer.displayLn("User created successfully!") }
    }

    @Test
    fun `should show error when username is empty`() = runTest{
        // Given:
        every { inputReader.readString() } returnsMany listOf("", "shahd", "123456789")

        // When:
        createMateUserUI.launchUi()

        // Then:
        verify { printer.displayLn("Input cannot be empty.") }
    }

    @Test
    fun `should show error when password is empty`() = runTest{
        // Given:
        every { inputReader.readString() } returnsMany listOf("shahd", "", "1223455")

        // When:
        createMateUserUI.launchUi()

        // Then:
        verify { printer.displayLn("Input cannot be empty.") }
    }

    @Test
    fun `should show error when user already exists`() = runTest{
        val name = "shahd"
        // Given:
        every { inputReader.readString() } returnsMany listOf("shahd", "123456")
        coEvery { useCase.invoke(any()) } throws UserAlreadyExistsException(name)

        // When:
        createMateUserUI.launchUi()

        // Then
        verify { printer.displayLn("Username '$name' already exists") }
    }

    @Test
    fun `should show error when username is null`() = runTest{
        // Given:
        every { inputReader.readString() } returnsMany listOf(null, "shahd", "123455")

        // When:
        createMateUserUI.launchUi()

        // Then:
        verify { printer.displayLn("Input cannot be empty.") }
    }

    @Test
    fun `should throw exception when username is empty`() = runTest{
        // Given:
        every { inputReader.readString() } returnsMany listOf("", "123456")
        coEvery { useCase.invoke(any()) } throws EmptyUsernameException()

        // When:
        createMateUserUI.launchUi()

        // Then:
        verify { printer.displayLn("Input cannot be empty.") }
    }

    @Test
    fun `should show error for null username`() = runTest{
        // Given
        every { inputReader.readString() } returnsMany listOf(null, "somePass")

        // When:
        createMateUserUI.launchUi()

        // Then:
        verify { printer.displayLn("Input cannot be empty.") }
    }

    @Test
    fun `should show error for null password`() = runTest{
        // Given:
        every { inputReader.readString() } returnsMany listOf("shahd", null, "12343")

        // When:
        createMateUserUI.launchUi()

        // Then:
        verify { printer.displayLn("Input cannot be empty.") }
    }
}
