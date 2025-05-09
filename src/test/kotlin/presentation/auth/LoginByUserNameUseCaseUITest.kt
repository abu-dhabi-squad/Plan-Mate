package presentation.auth

import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import logic.authentication.LoginByUserNameUseCase
import logic.exceptions.UserNotFoundException
import logic.model.User
import logic.model.UserType
import presentation.admin.ConsoleAdminMenuView
import presentation.io.InputReader
import presentation.io.Printer
import presentation.user.ConsoleUserMenuView
import logic.user.SaveLoggedUserUseCase
import java.util.*

class LoginByUserNameUseCaseUITest {

    private lateinit var loginUseCase: LoginByUserNameUseCase
    private lateinit var inputReader: InputReader
    private lateinit var printer: Printer
    private lateinit var consoleMenuViewUser: ConsoleUserMenuView
    private lateinit var consoleMenuViewAdmin: ConsoleAdminMenuView
    private lateinit var loginUi: LoginByUserNameUseCaseUI
    private lateinit var saveLoggedUserUseCase: SaveLoggedUserUseCase

    @BeforeEach
    fun setUp() {
        loginUseCase = mockk(relaxed = true)
        inputReader = mockk(relaxed = true)
        printer = mockk(relaxed = true)
        consoleMenuViewUser = mockk(relaxed = true)
        consoleMenuViewAdmin = mockk(relaxed = true)
        saveLoggedUserUseCase= mockk(relaxed = true)

        loginUi = LoginByUserNameUseCaseUI(
            loginUseCase,
            saveLoggedUserUseCase,
            inputReader,
            printer,
            consoleMenuViewUser,
            consoleMenuViewAdmin,
        )
    }


    @Test
    fun `should show error for empty username`() = runTest{
        // Given
        every { inputReader.readString() } returnsMany listOf("", "shahd", "12345678")

        // When
        loginUi.launchUi()

        // Then
        verify { printer.displayLn("Input cannot be empty.") }
    }

    @Test
    fun `should show error for empty password`() = runTest{
        // Given
        every { inputReader.readString() } returnsMany listOf("someUser", "", "12345678")

        // When
        loginUi.launchUi()

        // Then
        verify { printer.displayLn("Input cannot be empty.") }
    }

    @Test
    fun `should login successfully as ADMIN and launch admin menu`() =runTest{
        // Given
        val user = User(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "adminUser", "adminPass", UserType.ADMIN)
        every { inputReader.readString() } returnsMany listOf("adminUser", "adminPass")
        coEvery { loginUseCase.invoke("adminUser", "adminPass") } returns user

        // When
        loginUi.launchUi()

        // Then
        verify { printer.displayLn("Login successful! Welcome adminUser [ADMIN]") }
        coVerify { consoleMenuViewAdmin.launchUi() }
        coVerify(exactly = 0) { consoleMenuViewUser.launchUi() }
    }

    @Test
    fun `should login successfully as MATE and launch user menu`() = runTest{
        // Given
        val user = User(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "mateUser", "matePass", UserType.MATE)
        every { inputReader.readString() } returnsMany listOf("mateUser", "matePass")
        coEvery { loginUseCase.invoke("mateUser", "matePass") } returns user

        // When
        loginUi.launchUi()

        // Then
        verify { printer.displayLn("Login successful! Welcome mateUser [MATE]") }
        coVerify { consoleMenuViewUser.launchUi() }
        coVerify(exactly = 0) { consoleMenuViewAdmin.launchUi() }
    }

    @Test
    fun `should show error message when login fails with UserNotFoundException`() = runTest{
        // Given
        val username = "wrongUser"
        every { inputReader.readString() } returnsMany listOf(username, "wrongPass")
        coEvery { loginUseCase.invoke(username, "wrongPass") } throws UserNotFoundException(username)

        // When
        loginUi.launchUi()

        // Then
        verify {printer.displayLn(match{it.toString().contains("Login failed:User with username '$username' not found")})}
    }

    @Test
    fun `should show error for null username`() = runTest{
        // Given
        every { inputReader.readString() } returnsMany listOf(null, "shahd", "12345678")

        // When
        loginUi.launchUi()

        // Then
        verify { printer.displayLn("Input cannot be empty.") }
    }

    @Test
    fun `should show error for null password`() = runTest {
        // Given
        every { inputReader.readString() } returnsMany listOf("shahd", null, "12345678")

        // When
        loginUi.launchUi()

        // Then
        verify { printer.displayLn("Input cannot be empty.") }
    }

}








