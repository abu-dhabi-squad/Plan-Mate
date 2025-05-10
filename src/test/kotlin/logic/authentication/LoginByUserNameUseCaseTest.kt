package logic.authentication

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import logic.validation.PasswordValidator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import logic.exceptions.EmptyUsernameException
import logic.exceptions.InvalidCredentialsException
import logic.exceptions.UserNotFoundException
import logic.model.User
import logic.model.UserType
import logic.repository.AuthenticationRepository
import java.util.*
import kotlin.test.Test

class LoginByUserNameUseCaseTest {
    private lateinit var authRepository: AuthenticationRepository
    private lateinit var loginByUserNameUseCase: LoginByUserNameUseCase
    private lateinit var passwordValidator: PasswordValidator

    @BeforeEach
    fun setup() {
        authRepository = mockk(relaxed = true)
        passwordValidator = mockk(relaxed = true)
        loginByUserNameUseCase = LoginByUserNameUseCase(authRepository, passwordValidator)
    }

    @Test
    fun `getUser should return user when credentials are valid`() = runTest {
        val username = "testUser"
        val password = "correctPassword"
        val expectedUser = User(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), username, password, UserType.MATE)

        coEvery { authRepository.loginUser(username, password) } returns expectedUser

        val result = loginByUserNameUseCase(username, password)

        assertThat(result).isEqualTo(expectedUser)
        coVerify {
            authRepository.loginUser(username, password)
        }
    }

    @Test
    fun `getUser should throw UserNotFoundException when user not found`() = runTest {
        val username = "nonExistingUser"
        val password = "anyPassword"

        coEvery { authRepository.loginUser(username, password) } returns null

        assertThrows<UserNotFoundException> {
            loginByUserNameUseCase(username, password)
        }

        coVerify {
            authRepository.loginUser(username, password)
        }
    }

    @Test
    fun `getUser should hash password before calling repository`() = runTest {
        val username = "testUser"
        val password = "password123"
        val expectedUser = User(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), username, password, UserType.MATE)

        coEvery { authRepository.loginUser(username, password) } returns expectedUser

        loginByUserNameUseCase(username, password)

        coVerify(exactly = 1) { authRepository.loginUser(username, password) }
    }

    @Test
    fun `getUser should propagate InvalidCredentialsException from repository`() = runTest {
        val username = "testUser"
        val password = "wrongPassword"

        coEvery { authRepository.loginUser(username, password) } throws InvalidCredentialsException()

        assertThrows<InvalidCredentialsException> {
            loginByUserNameUseCase(username, password)
        }
    }

    @Test
    fun `createUser should call password validator`() = runTest {
        val user = User(
            username = "newUser",
            password = "ValidPass123!",
            userType = UserType.MATE
        )
        every { passwordValidator.validatePassword(any()) } just runs

        loginByUserNameUseCase(user.username, user.password)

        verify { passwordValidator.validatePassword(user.password) }
    }

    @Test
    fun `createUser should throw EmptyUsernameException when username is blank`() = runTest {
        val user = User(
            username = "",
            password = "ValidPass123!",
            userType = UserType.MATE
        )

        assertThrows<EmptyUsernameException> {
            loginByUserNameUseCase(user.username, user.password)
        }
    }
}