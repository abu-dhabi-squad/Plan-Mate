package logic.authentication

import com.google.common.truth.Truth.assertThat
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import squad.abudhabi.logic.authentication.CreateMateUserUseCase
import squad.abudhabi.logic.exceptions.InvalidPasswordException
import squad.abudhabi.logic.exceptions.UserAlreadyExistsException
import squad.abudhabi.logic.model.User
import squad.abudhabi.logic.model.UserType
import squad.abudhabi.logic.repository.AuthenticationRepository
import squad.abudhabi.logic.utils.HashingService
import squad.abudhabi.logic.validation.PasswordValidator

class CreateMateUserUseCaseTest {

    private lateinit var authRepository: AuthenticationRepository
    private lateinit var hashingService: HashingService
    private lateinit var passwordValidator: PasswordValidator
    private lateinit var createMateUserUseCase: CreateMateUserUseCase

    @BeforeEach
    fun setUp() {
        authRepository = mockk(relaxed = true)
        hashingService = mockk(relaxed = true)
        passwordValidator = mockk(relaxed = true)
        createMateUserUseCase = CreateMateUserUseCase(authRepository, hashingService, passwordValidator)
    }

    @Test
    fun `create should successfully add new user when inputs are valid`() {
        // Given
        val username = "shahd"
        val password = "pass123"
        val hashedPassword = "hashed_pass123"
        val userType = UserType.MATE
        val expectedUser = User(username = username, password = hashedPassword, userType = userType)

        every { passwordValidator.validatePassword(password) } just runs
        every { hashingService.hash(password) } returns hashedPassword
        every { authRepository.getUserByName(username) } returns null
        every { authRepository.createUser(any()) } just Runs

        // When
        createMateUserUseCase.create(username, password, UserType.MATE)

        // Then
        verify {
            passwordValidator.validatePassword(password)
            hashingService.hash(password)
            authRepository.getUserByName(username)
            authRepository.createUser(
                match {
                    it.username == expectedUser.username &&
                            it.password == expectedUser.password &&
                            it.userType == expectedUser.userType
                }
            )
        }
    }

    @Test
    fun `should throw IllegalArgumentException when username is blank`() {
        // Given
        val username = ""
        val password = "ValidPass123!"

        // When & Then
        val exception = assertThrows<IllegalArgumentException> {
            createMateUserUseCase.create(username, password, UserType.MATE)
        }
        assertThat(exception).hasMessageThat().contains("Username cannot be empty")
    }

    @Test
    fun `should throw InvalidPasswordException when password is invalid`() {
        // Given
        val username = "newUser"
        val weakPassword = "weak"
        every { passwordValidator.validatePassword(any()) } throws InvalidPasswordException("Invalid password")

        // When & Then
             assertThrows<InvalidPasswordException> {
            createMateUserUseCase.create(username, weakPassword, UserType.MATE)
        }
    }

    @Test
    fun `should throw UserAlreadyExistsException when user already exists`() {
        // Given
        val username = "existingUser"
        val password = "ValidPass123!"
        val existingUser = User(id = "2", username = username, password = "oldHash", userType = UserType.MATE)

        every { passwordValidator.validatePassword(any()) } just runs
        every { hashingService.hash(any()) } returns "hashedPassword"
        every { authRepository.getUserByName(username) } returns existingUser

        // When & Then
             assertThrows<UserAlreadyExistsException> {
            createMateUserUseCase.create(username, password, UserType.MATE)
        }
    }
}
