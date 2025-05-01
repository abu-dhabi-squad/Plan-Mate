package logic.authentication

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import squad.abudhabi.logic.authentication.LoginByUserNameUseCase
import squad.abudhabi.logic.exceptions.InvalidCredentialsException
import squad.abudhabi.logic.exceptions.UserNotFoundException
import squad.abudhabi.logic.model.User
import squad.abudhabi.logic.model.UserType
import squad.abudhabi.logic.repository.AuthenticationRepository
import squad.abudhabi.logic.utils.HashingService
import kotlin.test.Test

class LoginByUserNameUseCaseTest {
    private lateinit var authRepository: AuthenticationRepository
    private lateinit var hashingService: HashingService
    private lateinit var loginByUserNameUseCase: LoginByUserNameUseCase

    @BeforeEach
    fun setup() {
        authRepository = mockk()
        hashingService = mockk()
        loginByUserNameUseCase = LoginByUserNameUseCase(authRepository,hashingService)
    }

    @Test
    fun `should return user when credentials are correct`() {
        // Given
        val username = "shahd"
        val password = "pass123"
        val hashedPassword = "hashed_pass123"
        val expectedUser = User(username = username, password = hashedPassword, userType = UserType.MATE)

        every { authRepository.getUserByName(username) } returns expectedUser
        every { hashingService.hash(password) } returns hashedPassword

        // When
        val result = loginByUserNameUseCase.login(username, password)

        // Then
        assertThat(result).isEqualTo(expectedUser)
        verify { authRepository.getUserByName(username) }
        verify { hashingService.hash(password) }
    }


    @Test
    fun `should throw UserNotFoundException when user is not found`() {
        // Given
        val username = "shahd"
        val password = "pass123"
        every { authRepository.getUserByName(username) } returns null

        // When & Then
        val exception = assertThrows<UserNotFoundException> {
            loginByUserNameUseCase.login(username, password)
        }
        assertThat(exception).hasMessageThat().contains(username)
        verify { authRepository.getUserByName(username) }
    }

    @Test
    fun `should throw InvalidCredentialsException when password is incorrect`() {
        // Given
        val username = "shahd"
        val password = "wrongPassword"
        val existingUser = User(username = username, password = "hashed_pass123", userType = UserType.MATE)

        every { authRepository.getUserByName(username) } returns existingUser
        every { hashingService.hash(password) } returns "hashed_wrongPassword"

        // When & Then
        val exception = assertThrows<InvalidCredentialsException> {
            loginByUserNameUseCase.login(username, password)
        }
        assertThat(exception).hasMessageThat().contains("Invalid credentials")
        verify { authRepository.getUserByName(username) }
        verify { hashingService.hash(password) }
    }

}