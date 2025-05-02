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
    private lateinit var createMateUserUI:CreateMateUserUseCaseUI

    @BeforeEach
    fun setup() {
        useCase = mockk(relaxed = true)
        inputReader = mockk(relaxed = true)
        printer = mockk(relaxed = true)
        createMateUserUI = CreateMateUserUseCaseUI(useCase, inputReader, printer)
    }

    @Test
    fun `should create user successfully`() {
        every { inputReader.readString() } returnsMany listOf("shahd", "123456")

        createMateUserUI.launchUi()

        verify {
            useCase.create(
                match {
                    it.username == "shahd" &&
                            it.password == "123456" &&
                            it.userType == UserType.MATE
                }
            )
        }
        verify { printer.displayLn("User created successfully!") }
    }


    @Test
    fun `should show error when username is empty`() {
        every { inputReader.readString() } returnsMany listOf("", "shahd","123456789")

        createMateUserUI.launchUi()

        verify { printer.displayLn("Input cannot be empty.") }
    }

    @Test
    fun `should show error when password is empty`() {
        every { inputReader.readString() } returnsMany listOf("shahd", "", "1223455")

        createMateUserUI.launchUi()

        verify { printer.displayLn("Input cannot be empty.") }
    }



    @Test
    fun `should show error when user already exists`() {
        val name="shahd"
        every { inputReader.readString() } returnsMany listOf("shahd", "123456")
        every { useCase.create(any()) } throws UserAlreadyExistsException(name)

        createMateUserUI.launchUi()

        verify { printer.displayLn("User already exists:Username '$name' already exists") }
    }

    @Test
    fun `should show error when username is null`() {
        every { inputReader.readString() } returnsMany listOf(null, "shahd", "123455")

        createMateUserUI.launchUi()

        verify { printer.displayLn("Input cannot be empty.") }
    }

    @Test
    fun `should throw exception when username is empty `() {
        every { inputReader.readString() } returnsMany listOf("", "123456")
        every { useCase.create(any()) } throws EmptyUsernameException()

        createMateUserUI.launchUi()

        verify { printer.displayLn("Input cannot be empty.") }
    }

    @Test
    fun `should show error for null username`() {
        every { inputReader.readString() } returnsMany listOf(null, "somePass")

        createMateUserUI.launchUi()

        verify { printer.displayLn("Input cannot be empty.") }
    }

    @Test
    fun `should show error for null password`() {
        every { inputReader.readString() } returnsMany listOf("shahd", null,"12343")

        createMateUserUI.launchUi()

        verify { printer.displayLn("Input cannot be empty.") }
    }
}
