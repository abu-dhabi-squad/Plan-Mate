package logic.authentication

import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import logic.authentication.validtion.PasswordValidator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import logic.exceptions.EmptyUsernameException
import logic.exceptions.UserAlreadyExistsException
import logic.model.User
import logic.model.UserType
import logic.repository.AuthenticationRepository
import logic.utils.hashing.HashingService

class CreateMateUserUseCaseTest {
    private lateinit var authRepository: AuthenticationRepository
    private lateinit var passwordValidator: PasswordValidator
    private lateinit var createMateUserUseCase: CreateMateUserUseCase
    private lateinit var hashingPassword: HashingService

    @BeforeEach
    fun setUp() {
        authRepository = mockk(relaxed = true)
        passwordValidator = mockk(relaxed = true)
        hashingPassword = mockk(relaxed = true)
        createMateUserUseCase = CreateMateUserUseCase(authRepository, passwordValidator, hashingPassword)
    }

    @Test
    fun `should successfully create user when the input is valid`() = runTest {
        // Given
        val username = "shahd"
        val hashingPassword = hashingPassword.hash("pass123")
        val userType = UserType.MATE
        val inputUser = User(username = username, password = hashingPassword, userType = userType)
        every { passwordValidator.validatePassword(hashingPassword) } just Runs
        coEvery { authRepository.getUserByName(username) } returns null

        // When
        createMateUserUseCase(inputUser)

        // Then
        coVerify {
            authRepository.createUser(
                match { savedUser ->
                    savedUser.username == username &&
                            savedUser.password == hashingPassword &&
                            savedUser.userType == userType
                }
            )
        }
    }

    @Test
    fun `should hash the password before saving the user`() = runTest {
        // Given
        val plainPassword = "MyPlainPassword123"
        val user = User(
            username = "hashTestUser",
            password = plainPassword,
            userType = UserType.MATE
        )
        coEvery { authRepository.getUserByName(user.username) } returns null
        every { passwordValidator.validatePassword(plainPassword) } just runs

        // When
        createMateUserUseCase(user)

        // Then
        verify { hashingPassword.hash(plainPassword) }
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
        assertThrows<EmptyUsernameException> {
            createMateUserUseCase(user)
        }
    }

    @Test
    fun `should throw UserAlreadyExistsException when user exists`() = runTest {
        // Given
        val user = User(
            username = "existingUser",
            password = "ValidPass123!",
            userType = UserType.MATE
        )
        coEvery { authRepository.getUserByName(user.username) } returns user
        every { passwordValidator.validatePassword(any()) } returns Unit

        // When & Then
        assertThrows<UserAlreadyExistsException> {
            createMateUserUseCase(user)
        }
    }

    @Test
    fun `should call password validator`() = runTest {
        // Given
        val user = User(
            username = "newUser",
            password = "ValidPass123!",
            userType = UserType.MATE
        )
        coEvery { authRepository.getUserByName(any()) } returns null
        every { passwordValidator.validatePassword(any()) } just runs

        // When
        createMateUserUseCase(user)

        // Then
        verify { passwordValidator.validatePassword(user.password) }
    }
}
