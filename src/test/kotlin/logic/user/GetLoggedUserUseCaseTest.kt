package logic.user

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import logic.exceptions.NoLoggedInUserException
import logic.model.User
import logic.model.User.UserType
import logic.repository.AuthenticationRepository
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class GetLoggedUserUseCaseTest {
    private lateinit var getLoggedUserUseCase: GetLoggedUserUseCase
    private lateinit var authenticationRepository: AuthenticationRepository

    @BeforeEach
    fun setup() {
        authenticationRepository = mockk(relaxed = true)
        getLoggedUserUseCase = GetLoggedUserUseCase(authenticationRepository)
    }

    @Test
    fun `should return logged user when getLoggedUser is called`() {
        // Given
        val user = User(
            username = "newUser",
            password = "ValidPass123!",
            userType = UserType.MATE
        )
        every { authenticationRepository.getLoggedUser() } returns user

        // When
        val res = getLoggedUserUseCase()

        // Then
        assertThat(res).isEqualTo(user)
        verify { authenticationRepository.getLoggedUser() }
    }

    @Test
    fun `should throw exception when no logged user is found`() {
        // Given
        every { authenticationRepository.getLoggedUser() } returns null

        // When & Then
        assertThrows<NoLoggedInUserException> {
            getLoggedUserUseCase()
        }
    }
}