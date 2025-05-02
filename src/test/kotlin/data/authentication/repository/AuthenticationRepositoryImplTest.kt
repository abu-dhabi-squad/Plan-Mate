import com.google.common.truth.Truth
import data.TestData.user1
import data.TestData.user2
import data.TestData.userName1
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import squad.abudhabi.data.authentication.datasource.AuthenticationDataSource
import squad.abudhabi.data.authentication.repository.AuthenticationRepositoryImpl
import squad.abudhabi.logic.exceptions.UserAlreadyExistsException
import squad.abudhabi.logic.exceptions.UserNotFoundException
import kotlin.test.assertFailsWith


class AuthenticationRepositoryImplTest {


    private lateinit var authenticationDataSource: AuthenticationDataSource
    private lateinit var authenticationRepository: AuthenticationRepositoryImpl

    @BeforeEach
    fun setup() {
        authenticationDataSource = mockk()
        authenticationRepository = AuthenticationRepositoryImpl(authenticationDataSource)
    }

    @Test
    fun `getUserByName should return user when user exists`() {
        // Given
        every { authenticationDataSource.getAllUsers() } returns listOf(user1, user2)

        // When
        val result = authenticationRepository.getUserByName(userName1)

        // Then
        Truth.assertThat(result).isEqualTo(user1)
    }

    @Test
    fun `getUserByName should throw UserNotFoundException when user no found`() {
        // Given
        every { authenticationDataSource.getAllUsers() } returns listOf(user1, user2)

        // When & Then
        assertFailsWith<UserNotFoundException> {
            authenticationRepository.getUserByName("user3")
        }
    }

    @Test
    fun `addNewUser should add user when user does not exist`() {
        // Given
        every { authenticationDataSource.getAllUsers() } returns listOf(user1)
        every { authenticationDataSource.saveUsers(any()) } just Runs

        // When
        authenticationRepository.addNewUser(user2)

        // Then
        verify(exactly = 1) { authenticationDataSource.saveUsers(listOf(user1, user2)) }
    }

    @Test
    fun `addNewUser should throw UserAlreadyExistsException when user already exists`() {
        // Given
        every { authenticationDataSource.getAllUsers() } returns listOf(user1, user2)

        // When & Then
        assertFailsWith<UserAlreadyExistsException> {
            authenticationRepository.addNewUser(user1)
        }
    }

    @Test
    fun `addNewUser should check if the user already exists before adding`() {
        // Given
        every { authenticationDataSource.getAllUsers() } returns listOf(user1)
        every { authenticationDataSource.saveUsers(any()) } just Runs

        // When
        authenticationRepository.addNewUser(user2)

        // Then
        verify(exactly = 1) { authenticationDataSource.saveUsers(listOf(user1, user2)) }
    }

}