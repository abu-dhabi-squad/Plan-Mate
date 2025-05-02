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
    fun `getUser should return user when credentials are valid`() {
        val username = "testUser"
        val password = "correctPassword"
        val hashedPassword = "hashedPassword"
        val expectedUser = User(username, hashedPassword, "role", UserType.MATE)

        every { hashingService.hash(password) } returns hashedPassword
        every { authRepository.loginUser(username, hashedPassword) } returns expectedUser

        val result = loginByUserNameUseCase(username, password)

        assertThat(result).isEqualTo(expectedUser)
        verify {
            hashingService.hash(password)
            authRepository.loginUser(username, hashedPassword)
        }
    }

    @Test
    fun `getUser should throw UserNotFoundException when user not found`() {
        val username = "nonExistingUser"
        val password = "anyPassword"
        val hashedPassword = "hashedPassword"

        every { hashingService.hash(password) } returns hashedPassword
        every { authRepository.loginUser(username, hashedPassword) } returns null

        assertThrows<UserNotFoundException> {
            loginByUserNameUseCase(username, password)
        }

        verify {
            hashingService.hash(password)
            authRepository.loginUser(username, hashedPassword)
        }
    }

    @Test
    fun `getUser should hash password before calling repository`() {
        val username = "testUser"
        val password = "password123"
        val hashedPassword = "hashed123"
        val expectedUser = User(username, hashedPassword, "role", UserType.MATE)

        every { hashingService.hash(password) } returns hashedPassword
        every { authRepository.loginUser(username, hashedPassword) } returns expectedUser

        loginByUserNameUseCase(username, password)

        verify(exactly = 1) { hashingService.hash(password) }
        verify(exactly = 1) { authRepository.loginUser(username, hashedPassword) }
    }

    @Test
    fun `getUser should propagate InvalidCredentialsException from repository`() {
        val username = "testUser"
        val password = "wrongPassword"
        val hashedPassword = "hashedWrong"

        every { hashingService.hash(password) } returns hashedPassword
        every { authRepository.loginUser(username, hashedPassword) } throws InvalidCredentialsException()

        assertThrows<InvalidCredentialsException> {
            loginByUserNameUseCase(username, password)
        }
    }

}