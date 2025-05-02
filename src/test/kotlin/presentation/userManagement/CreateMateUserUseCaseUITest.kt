package presentation.userManagement

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import squad.abudhabi.logic.authentication.CreateMateUserUseCase
import squad.abudhabi.logic.exceptions.EmptyUsernameException
import squad.abudhabi.logic.exceptions.UserAlreadyExistsException
import squad.abudhabi.logic.model.UserType
import squad.abudhabi.presentation.ui_io.InputReader
import squad.abudhabi.presentation.ui_io.Printer
import squad.abudhabi.presentation.userManagement.CreateMateUserUseCaseUI
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
    fun `should create user successfully`() {
        // Given
        every { inputReader.readString() } returnsMany listOf("shahd", "123456")

        // When: Launch the UI to create the user
        createMateUserUI.launchUi()

        // Then:
        verify {
            useCase.create(
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
    fun `should show error when username is empty`() {
        // Given:
        every { inputReader.readString() } returnsMany listOf("", "shahd", "123456789")

        // When:
        createMateUserUI.launchUi()

        // Then:
        verify { printer.displayLn("Input cannot be empty.") }
    }

    @Test
    fun `should show error when password is empty`() {
        // Given:
        every { inputReader.readString() } returnsMany listOf("shahd", "", "1223455")

        // When:
        createMateUserUI.launchUi()

        // Then: Verify the error message for empty input
        verify { printer.displayLn("Input cannot be empty.") }
    }

    @Test
    fun `should show error when user already exists`() {
        val name = "shahd"
        // Given:
        every { inputReader.readString() } returnsMany listOf("shahd", "123456")
        every { useCase.create(any()) } throws UserAlreadyExistsException(name)

        // When:
        createMateUserUI.launchUi()

        // Then
        verify { printer.displayLn("User already exists:Username '$name' already exists") }
    }

    @Test
    fun `should show error when username is null`() {
        // Given:
        every { inputReader.readString() } returnsMany listOf(null, "shahd", "123455")

        // When:
        createMateUserUI.launchUi()

        // Then:
        verify { printer.displayLn("Input cannot be empty.") }
    }

    @Test
    fun `should throw exception when username is empty`() {
        // Given:
        every { inputReader.readString() } returnsMany listOf("", "123456")
        every { useCase.create(any()) } throws EmptyUsernameException()

        // When:
        createMateUserUI.launchUi()

        // Then:
        verify { printer.displayLn("Input cannot be empty.") }
    }

    @Test
    fun `should show error for null username`() {
        // Given
        every { inputReader.readString() } returnsMany listOf(null, "somePass")

        // When:
        createMateUserUI.launchUi()

        // Then:
        verify { printer.displayLn("Input cannot be empty.") }
    }

    @Test
    fun `should show error for null password`() {
        // Given:
        every { inputReader.readString() } returnsMany listOf("shahd", null, "12343")

        // When:
        createMateUserUI.launchUi()

        // Then:
        verify { printer.displayLn("Input cannot be empty.") }
    }
}
