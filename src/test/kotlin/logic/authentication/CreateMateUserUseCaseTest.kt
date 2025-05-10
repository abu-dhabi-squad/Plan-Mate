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
import logic.validation.PasswordValidator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import logic.exceptions.EmptyUsernameException
import logic.exceptions.UserAlreadyExistsException
import logic.model.User
import logic.model.UserType
import logic.repository.AuthenticationRepository
class CreateMateUserUseCaseTest {
    private lateinit var authRepository: AuthenticationRepository
    private lateinit var passwordValidator: PasswordValidator
    private lateinit var createMateUserUseCase: CreateMateUserUseCase
    @BeforeEach
    fun setUp() {
        authRepository = mockk(relaxed = true)
        passwordValidator = mockk(relaxed = true)
        createMateUserUseCase = CreateMateUserUseCase(authRepository, passwordValidator)
    }
    @Test
    fun `createUser should successfully create user with valid input`() = runTest {
        val username = "shahd"
        val password = "pass123"
        val userType = UserType.MATE
        val inputUser = User(username = username, password = password, userType = userType)
        every { passwordValidator.validatePassword(password) } just Runs
        coEvery { authRepository.getUserByName(username) } returns null
        createMateUserUseCase(inputUser)
        coVerify {
            authRepository.createUser(
                match { savedUser ->
                    savedUser.username == username &&
                            savedUser.password == password &&
                            savedUser.userType == userType
                }
            )
        }
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
    fun `createUser should throw UserAlreadyExistsException when user exists`() = runTest{
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
    fun `createUser should call password validator`() = runTest{
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
