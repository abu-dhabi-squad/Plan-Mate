package logic.authentication

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import logic.exceptions.InvalidCredentialsException
import logic.model.User
import logic.model.User.UserType
import logic.repository.AuthenticationRepository
import logic.utils.hashing.HashingService
import java.util.UUID
import kotlin.test.Test

class LoginByUserNameUseCaseTest {
    private lateinit var authRepository: AuthenticationRepository
    private lateinit var loginByUserNameUseCase: LoginByUserNameUseCase
    private lateinit var hashingPassword: HashingService

    @BeforeEach
    fun setup() {
        authRepository = mockk(relaxed = true)
        hashingPassword = mockk(relaxed = true)
        loginByUserNameUseCase = LoginByUserNameUseCase(authRepository, hashingPassword)
    }

    @Test
    fun `should return user when credentials are valid`() = runTest {
        // Given
        val username = "testUser"
        val password = "correctPassword"
        val hashedPassword = "hashedCorrectPassword"
        val expectedUser = User(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), username, hashedPassword, UserType.MATE)
        every { hashingPassword.hash(password) } returns hashedPassword
        coEvery { authRepository.getUserByName(username) } returns expectedUser

        // When
        val result = loginByUserNameUseCase(username, password)

        // Then
        assertThat(result).isEqualTo(expectedUser)
        coVerify {
            authRepository.getUserByName(username)
        }
    }

    @Test
    fun `should throw UserNotFoundException when user not found`() = runTest {
        // Given
        val username = "nonExistingUser"
        val password = "anyPassword"
        val hashedPassword = "hashedAnyPassword"
        every { hashingPassword.hash(password) } returns hashedPassword
        coEvery { authRepository.getUserByName(username) } returns null

        // When & Then
        assertThrows<InvalidCredentialsException> {
            loginByUserNameUseCase(username, password)
        }
        coVerify {
            authRepository.getUserByName(username)
        }
    }

    @Test
    fun `should hash password before calling repository`() = runTest {
        // Given
        val username = "testUser"
        val password = "password123"
        val hashedPassword = "hashedPassword123"
        val expectedUser = User(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), username, hashedPassword, UserType.MATE)
        every { hashingPassword.hash(password) } returns hashedPassword
        coEvery { authRepository.getUserByName(username) } returns expectedUser

        // When
        loginByUserNameUseCase(username, password)

        // Then
        coVerify(exactly = 1) { hashingPassword.hash(password) }
        coVerify(exactly = 1) { authRepository.getUserByName(username) }
    }

    @Test
    fun `should throw exception when repository fail that is verify that the exception will be thrown from use case and not handled in it`() = runTest {
        // Given
        val username = "testUser"
        val password = "wrongPassword"
        val hashedPassword = "hashedWrongPassword"
        every { hashingPassword.hash(password) } returns hashedPassword
        coEvery { authRepository.getUserByName(username) } throws InvalidCredentialsException()

        // When & Then
        assertThrows<InvalidCredentialsException> {
            loginByUserNameUseCase(username, password)
        }
        coVerify { hashingPassword.hash(password) }
        coVerify { authRepository.getUserByName(username) }
    }

    @Test
    fun `should throw EmptyUsernameException when username is blank`() = runTest {
        // Given
        val user = User(
            username = "",
            password = "ValidPass123!",
            userType = UserType.MATE
        )

        // When & Then
        assertThrows<InvalidCredentialsException> {
            loginByUserNameUseCase(user.username, user.password)
        }
    }
}