package presentation.auth

import helper.createUser
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import logic.authentication.LoginByUserNameUseCase
import logic.model.UserType
import presentation.presentation.user.admin.ConsoleAdminMenuUI
import presentation.io.Printer
import presentation.presentation.user.mate.ConsoleUserMenuUI
import logic.user.SaveLoggedUserUseCase
import presentation.presentation.utils.PromptService

class LoginByUserNameUITest {

    private val loginUseCase: LoginByUserNameUseCase = mockk(relaxed = true)
    private val saveLoggedUserUseCase: SaveLoggedUserUseCase = mockk(relaxed = true)
    private val promptService: PromptService = mockk(relaxed = true)
    private val printer: Printer = mockk(relaxed = true)
    private val consoleMenuViewUser: ConsoleUserMenuUI = mockk(relaxed = true)
    private val consoleMenuViewAdmin: ConsoleAdminMenuUI = mockk(relaxed = true)
    private lateinit var ui: LoginByUserNameUI

    @BeforeEach
    fun setUp() {
        ui = LoginByUserNameUI(
            loginUseCase = loginUseCase,
            saveLoggedUserUseCase = saveLoggedUserUseCase,
            promptService = promptService,
            printer = printer,
            consoleMenuViewUser = consoleMenuViewUser,
            consoleMenuViewAdmin = consoleMenuViewAdmin,
        )
    }

    @Test
    fun `launchUi should call consoleMenuViewAdmin launchUi when admin logged successfully`() = runTest {
        //Given
        val user = createUser(userType = UserType.ADMIN)
        coEvery { loginUseCase(any(),any()) } returns user
        //When
        ui.launchUi()
        //Then
        verify { printer.displayLn(match{it.toString().contains("Login successful")}) }
        verify { saveLoggedUserUseCase(user) }
        coVerify { consoleMenuViewAdmin.launchUi() }
    }

    @Test
    fun `launchUi should call consoleMenuViewUser launchUi when mate logged successfully`() = runTest {
        //Given
        val user = createUser(userType = UserType.MATE)
        coEvery { loginUseCase(any(),any()) } returns user
        //When
        ui.launchUi()
        //Then
        verify { printer.displayLn(match{it.toString().contains("Login successful")}) }
        verify { saveLoggedUserUseCase(user) }
        coVerify { consoleMenuViewUser.launchUi() }
    }

    @Test
    fun `launchUi should print error message when loginUseCase throw exception`() = runTest {
        //Given
        coEvery { loginUseCase(any(),any()) } throws Exception()
        //When
        ui.launchUi()
        //Then
        verify { printer.displayLn(match { it.toString().contains("${Exception().message}") }) }
    }

    @Test
    fun `launchUi should print error message when saveLoggedUserUseCase throw exception`() = runTest {
        //Given
        val user = createUser(userType = UserType.MATE)
        coEvery { loginUseCase(any(),any()) } returns user
        coEvery { saveLoggedUserUseCase(user) } throws Exception()
        //When
        ui.launchUi()
        //Then
        verify { printer.displayLn(match { it.toString().contains("${Exception().message}") }) }
    }
}