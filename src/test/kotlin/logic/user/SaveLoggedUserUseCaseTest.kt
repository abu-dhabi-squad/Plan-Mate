package logic.user

import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import logic.exceptions.EmptyUsernameException
import logic.model.User
import logic.model.User.UserType
import logic.repository.AuthenticationRepository
import kotlin.test.Test

class SaveLoggedUserUseCaseTest {
    private lateinit var saveLoggedUserUseCase: SaveLoggedUserUseCase
    private lateinit var authenticationRepository: AuthenticationRepository

    @BeforeEach
    fun setup() {
        authenticationRepository = mockk(relaxed = true)
        saveLoggedUserUseCase = SaveLoggedUserUseCase(authenticationRepository)
    }

    @Test
    fun `should throw EmptyUserUserException when username is empty`() {
        // Given
        val user = User(
            username = "",
            password = "ValidPass123!",
            userType = UserType.MATE
        )

        // When & Then
        assertThrows<EmptyUsernameException> {
            saveLoggedUserUseCase(user)
        }
    }

    @Test
    fun `should throw EmptyUserUserException when pass is empty`() {
        // Given
        val user = User(
            username = "user",
            password = "",
            userType = UserType.MATE
        )

        // When & Then
        assertThrows<EmptyUsernameException> {
            saveLoggedUserUseCase(user)
        }
    }

    @Test
    fun `should complete when username and pass are valid`() {
        // Given
        val user = User(
            username = "user",
            password = "12345678nN#",
            userType = UserType.MATE
        )
        every { authenticationRepository.saveLoggedUser(user) } just runs

        // When
        saveLoggedUserUseCase(user)

        // Then
        verify { saveLoggedUserUseCase(user) }
    }
}