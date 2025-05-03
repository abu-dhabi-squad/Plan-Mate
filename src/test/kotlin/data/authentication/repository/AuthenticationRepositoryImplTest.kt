import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
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
import org.junit.jupiter.api.assertThrows
import squad.abudhabi.data.authentication.datasource.AuthenticationDataSource
import squad.abudhabi.data.authentication.datasource.LoggedUserDataSource
import squad.abudhabi.data.authentication.repository.AuthenticationRepositoryImpl
import squad.abudhabi.logic.exceptions.InvalidCredentialsException
import squad.abudhabi.logic.exceptions.UserAlreadyExistsException
import squad.abudhabi.logic.exceptions.UserNotFoundException
import squad.abudhabi.logic.model.User
import squad.abudhabi.logic.model.UserType
import kotlin.test.assertFailsWith


class AuthenticationRepositoryImplTest {


    private lateinit var authenticationDataSource: AuthenticationDataSource
    private lateinit var authenticationRepository: AuthenticationRepositoryImpl
    private lateinit var loggedUserDataSource: LoggedUserDataSource

    @BeforeEach
    fun setup() {
        authenticationDataSource = mockk(relaxed = true)
        loggedUserDataSource=mockk(relaxed = true)
        authenticationRepository = AuthenticationRepositoryImpl(authenticationDataSource,loggedUserDataSource)
    }

    @Test
    fun `login should return user when username and password are correct`() {
        // Given
        every { authenticationDataSource.getUserByUserName(user1.username) } returns user1

        // When
        val result = authenticationRepository.loginUser(user1.username, "pass1")

        // Then
        Truth.assertThat(result).isEqualTo(user1)
    }

    @Test
    fun `login should throw InvalidCredentialsException when password is incorrect`() {
        // Given
        every { authenticationDataSource.getUserByUserName(user1.username) } returns user1

        // When & Then
        assertThrows<InvalidCredentialsException> {
            authenticationRepository.loginUser(user1.username, "wrongPassword")
        }
    }

    @Test
    fun `login should throw InvalidCredentialsException when password is empty`() {
        every { authenticationDataSource.getUserByUserName(user1.username) } returns user1

        assertThrows<InvalidCredentialsException> {
            authenticationRepository.loginUser(user1.username, "")
        }
    }

    @Test
    fun `getUserByName should return user when username exists`() {
        // Given
        every { authenticationDataSource.getUserByUserName(user1.username) } returns user1

        // When
        val result = authenticationRepository.getUserByName(user1.username)

        // Then
        Truth.assertThat(result).isEqualTo(user1)
    }

    @Test
    fun `getUserByName should throw UserNotFoundException when username does not exist`() {
        // Given
        every { authenticationDataSource.getUserByUserName(user1.username) } returns null

        // When & Then
        assertThrows<UserNotFoundException> {
            authenticationRepository.getUserByName(user1.username)
        }
    }

    @Test
    fun `createUser should create user when username does not exist`() {
        // Given
        every { authenticationDataSource.getUserByUserName(user2.username) } returns null
        every { authenticationDataSource.createUser(user2) } just Runs

        // When
        authenticationRepository.createUser(user2)

        // Then
        verify(exactly = 1) { authenticationDataSource.createUser(user2) }
    }

    @Test
    fun `createUser should throw UserAlreadyExistsException when username already exists`() {
        // Given
        every { authenticationDataSource.getUserByUserName(user1.username) } returns user1

        // When & Then
        assertThrows<UserAlreadyExistsException> {
            authenticationRepository.createUser(user1)
        }
    }


    @Test
    fun `createUser should check if the user already exists before adding`() {
        // Given
        every { authenticationDataSource.getUserByUserName(user2.username) } returns null
        every { authenticationDataSource.createUser(user2) } just Runs

        // When
        authenticationRepository.createUser(user2)

        // Then
        verify(exactly = 1) { authenticationDataSource.createUser(user2) }
    }

    @Test
    fun `login should throw InvalidCredentialsException when user is not found`() {
        // Given
        every { authenticationDataSource.getUserByUserName(user1.username) } returns null

        // When & Then
        assertThrows<InvalidCredentialsException> {
            authenticationRepository.loginUser(user1.username, user1.password)
        }
    }

    @Test
    fun `saveLoggedUser should save user correctly`(){
        val user = User(
            username = "",
            password = "ValidPass123!",
            userType = UserType.MATE
        )
        every { loggedUserDataSource.getLoggedUser() } returns user
        authenticationRepository.saveLoggedUser(user)
        assertThat(authenticationRepository.getLoggedUser()).isEqualTo(user)

    }

    @Test
    fun `getLoggedUser should return null when no user was logged`(){

        every { loggedUserDataSource.getLoggedUser() } returns null
        val res=authenticationRepository.getLoggedUser()
        assertThat(res).isNull()
    }


}