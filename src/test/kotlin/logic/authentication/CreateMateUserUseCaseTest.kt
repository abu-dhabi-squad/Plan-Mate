package logic.authentication

import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import squad.abudhabi.logic.authentication.CreateMateUserUseCase
import squad.abudhabi.logic.exceptions.EmptyUsernameException
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
    fun `createUser should successfully create user with valid input`() {
        val username = "shahd"
        val password = "pass123"
        val hashedPassword = "hashed_pass123"
        val userType = UserType.MATE
        val inputUser = User(username = username, password = password, userType = userType)

        every { passwordValidator.validatePassword(password) } just Runs
        every { hashingService.hash(password) } returns hashedPassword
        every { authRepository.getUserByName(username) } returns null

        createMateUserUseCase.create(inputUser)

        verify {
            authRepository.createUser(
                match { savedUser ->
                    savedUser.username == username &&
                            savedUser.password == hashedPassword &&
                            savedUser.userType == userType
                }
            )
        }
    }

    @Test
    fun `createUser should throw EmptyUsernameException when username is blank`() {
        val user = User(
            username = "",
            password = "ValidPass123!",
            userType = UserType.MATE
        )

        assertThrows<EmptyUsernameException> {
            createMateUserUseCase.create(user)
        }
    }

    @Test
    fun `createUser should throw UserAlreadyExistsException when user exists`() {
        val user = User(
            username = "existingUser",
            password = "ValidPass123!",
            userType = UserType.MATE
        )

        every { authRepository.getUserByName(user.username) } returns user
        every { passwordValidator.validatePassword(any()) } returns Unit

        assertThrows<UserAlreadyExistsException> {
            createMateUserUseCase.create(user)
        }
    }
    @Test
    fun `createUser should call password validator`() {
        val user = User(
            username = "newUser",
            password = "ValidPass123!",
            userType = UserType.MATE
        )
        every { authRepository.getUserByName(any()) } returns null
        every { hashingService.hash(any()) } returns "hashedPassword"
        every { passwordValidator.validatePassword(any()) } just runs

        createMateUserUseCase.create(user)

        verify { passwordValidator.validatePassword(user.password) }
    }
}
