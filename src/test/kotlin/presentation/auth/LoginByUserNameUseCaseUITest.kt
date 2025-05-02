package presentation.auth

import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import squad.abudhabi.logic.authentication.LoginByUserNameUseCase
import squad.abudhabi.logic.exceptions.UserNotFoundException
import squad.abudhabi.logic.model.User
import squad.abudhabi.logic.model.UserType
import squad.abudhabi.presentation.admin.ConsoleAdminMenuView
import squad.abudhabi.presentation.auth.LoginByUserNameUseCaseUI
import squad.abudhabi.presentation.ui_io.InputReader
import squad.abudhabi.presentation.ui_io.Printer
import squad.abudhabi.presentation.user.ConsoleUserMenuView

class LoginByUserNameUseCaseUITest {

    private lateinit var loginUseCase: LoginByUserNameUseCase
    private lateinit var inputReader: InputReader
    private lateinit var printer: Printer
    private lateinit var consoleMenuViewUser: ConsoleUserMenuView
    private lateinit var consoleMenuViewAdmin: ConsoleAdminMenuView
    private lateinit var loginUi: LoginByUserNameUseCaseUI

    @BeforeEach
    fun setUp() {
        loginUseCase = mockk(relaxed = true)
        inputReader = mockk(relaxed = true)
        printer = mockk(relaxed = true)
        consoleMenuViewUser = mockk(relaxed = true)
        consoleMenuViewAdmin = mockk(relaxed = true)

        loginUi = LoginByUserNameUseCaseUI(
            loginUseCase,
            inputReader,
            printer,
            consoleMenuViewUser,
            consoleMenuViewAdmin
        )
    }


    @Test
    fun `should show error for empty username`() {
        // Given
        every { inputReader.readString() } returnsMany listOf("", "shahd", "12345678")

        // When
        loginUi.launchUi()

        // Then
        verify { printer.displayLn("Input cannot be empty.") }
    }

    @Test
    fun `should show error for empty password`() {
        // Given
        every { inputReader.readString() } returnsMany listOf("someUser", "", "12345678")

        // When
        loginUi.launchUi()

        // Then
        verify { printer.displayLn("Input cannot be empty.") }
    }

    @Test
    fun `should login successfully as ADMIN and launch admin menu`() {
        // Given
        val user = User("2", "adminUser", "adminPass", UserType.ADMIN)
        every { inputReader.readString() } returnsMany listOf("adminUser", "adminPass")
        every { loginUseCase.getUser("adminUser", "adminPass") } returns user

        // When
        loginUi.launchUi()

        // Then
        verify { printer.displayLn("Login successful! Welcome adminUser [ADMIN]") }
        verify { consoleMenuViewAdmin.launchUi() }
        verify(exactly = 0) { consoleMenuViewUser.launchUi() }
    }

    @Test
    fun `should login successfully as MATE and launch user menu`() {
        // Given
        val user = User("3", "mateUser", "matePass", UserType.MATE)
        every { inputReader.readString() } returnsMany listOf("mateUser", "matePass")
        every { loginUseCase.getUser("mateUser", "matePass") } returns user

        // When
        loginUi.launchUi()

        // Then
        verify { printer.displayLn("Login successful! Welcome mateUser [MATE]") }
        verify { consoleMenuViewUser.launchUi() }
        verify(exactly = 0) { consoleMenuViewAdmin.launchUi() }
    }

    @Test
    fun `should show error message when login fails with UserNotFoundException`() {
        // Given
        val username = "wrongUser"
        every { inputReader.readString() } returnsMany listOf(username, "wrongPass")
        every { loginUseCase.getUser(username, "wrongPass") } throws UserNotFoundException(username)

        // When
        loginUi.launchUi()

        // Then
        verify {printer.displayLn(match{it.toString().contains("Login failed:User with username '$username' not found")})}
    }

    @Test
    fun `should show error for null username`() {
        // Given
        every { inputReader.readString() } returnsMany listOf(null, "shahd", "12345678")

        // When
        loginUi.launchUi()

        // Then
        verify { printer.displayLn("Input cannot be empty.") }
    }

    @Test
    fun `should show error for null password`() {
        // Given
        every { inputReader.readString() } returnsMany listOf("shahd", null, "12345678")

        // When
        loginUi.launchUi()

        // Then
        verify { printer.displayLn("Input cannot be empty.") }
    }

}








