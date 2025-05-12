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
import presentation.logic.authentication.validtion.PasswordValidator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import logic.exceptions.EmptyUsernameException
import logic.exceptions.UserAlreadyExistsException
import logic.model.User
import logic.model.UserType
import logic.repository.AuthenticationRepository
import presentation.logic.utils.hashing.HashingService

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
    fun `createUser should successfully create user with valid input`() = runTest {
        val username = "shahd"
        val hashingPassword = hashingPassword.hash("pass123")
        val userType = UserType.MATE
        val inputUser = User(username = username, password = hashingPassword, userType = userType)
        every { passwordValidator.validatePassword(hashingPassword) } just Runs
        coEvery { authRepository.getUserByName(username) } returns null
        createMateUserUseCase(inputUser)
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
    fun `createUser should hash the password before saving the user`() = runTest {
        val plainPassword = "MyPlainPassword123"
        val user = User(
            username = "hashTestUser",
            password = plainPassword,
            userType = UserType.MATE
        )

        coEvery { authRepository.getUserByName(user.username) } returns null
        every { passwordValidator.validatePassword(plainPassword) } just runs

        createMateUserUseCase(user)

        verify { hashingPassword.hash(plainPassword) }
    }

    @Test
    fun `createUser should throw EmptyUsernameException when username is blank`() = runTest {
        val user = User(
            username = "",
            password = "ValidPass123!",
            userType = UserType.MATE
        )
        assertThrows<EmptyUsernameException> {
            createMateUserUseCase(user)
        }
    }

    @Test
    fun `createUser should throw UserAlreadyExistsException when user exists`() = runTest {
        val user = User(
            username = "existingUser",
            password = "ValidPass123!",
            userType = UserType.MATE
        )
        coEvery { authRepository.getUserByName(user.username) } returns user
        every { passwordValidator.validatePassword(any()) } returns Unit
        assertThrows<UserAlreadyExistsException> {
            createMateUserUseCase(user)
        }
    }

    @Test
    fun `createUser should call password validator`() = runTest {
        val user = User(
            username = "newUser",
            password = "ValidPass123!",
            userType = UserType.MATE
        )
        coEvery { authRepository.getUserByName(any()) } returns null
        every { passwordValidator.validatePassword(any()) } just runs
        createMateUserUseCase(user)
        verify { passwordValidator.validatePassword(user.password) }
    }
}
