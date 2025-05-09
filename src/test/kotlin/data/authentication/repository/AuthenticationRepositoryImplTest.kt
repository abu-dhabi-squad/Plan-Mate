import com.google.common.truth.Truth.assertThat
import data.TestData.user1
import data.authentication.mapper.MongoUserMapper
import data.authentication.model.UserDto
import data.authentication.repository.AuthenticationRepositoryImpl
import data.authentication.repository.LoggedUserDataSource
import data.authentication.repository.RemoteAuthenticationDataSource
import io.mockk.*
import kotlinx.coroutines.runBlocking
import logic.exceptions.InvalidCredentialsException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AuthenticationRepositoryImplTest {

    private lateinit var remoteDataSource: RemoteAuthenticationDataSource
    private lateinit var loggedUserDataSource: LoggedUserDataSource
    private lateinit var mapper: MongoUserMapper
    private lateinit var repository: AuthenticationRepositoryImpl

    private val userDto = UserDto(
        id = user1.id.toString(),
        username = user1.username,
        password = user1.password,
        userType = user1.userType.name
    )

    @BeforeEach
    fun setUp() {
        remoteDataSource = mockk()
        loggedUserDataSource = mockk(relaxed = true)
        mapper = mockk()
        repository = AuthenticationRepositoryImpl(remoteDataSource, loggedUserDataSource, mapper)
    }
    @Test
    fun `loginUser should return user when credentials match`() = runBlocking {
        // Given
        coEvery { remoteDataSource.getUserByUserName(user1.username) } returns userDto
        every { mapper.dtoToUser(userDto) } returns user1

        // When
        val result = repository.loginUser(user1.username, user1.password)

        // Then
        assertThat(result).isEqualTo(user1)
    }

    @Test
    fun `loginUser should throw InvalidCredentialsException when password does not match`(): Unit = runBlocking {
        // Given
        val wrongPasswordDto = userDto.copy(password = "wrongpass")
        coEvery { remoteDataSource.getUserByUserName(user1.username) } returns wrongPasswordDto

        // Then
        try {
            repository.loginUser(user1.username, user1.password)
        } catch (e: InvalidCredentialsException) {
            assertThat(e).isInstanceOf(InvalidCredentialsException::class.java)
        }
    }

    @Test
    fun `loginUser should throw InvalidCredentialsException when user not found`(): Unit = runBlocking {
        // Given
        coEvery { remoteDataSource.getUserByUserName(user1.username) } returns null

        // Then
        try {
            repository.loginUser(user1.username, user1.password)
        } catch (e: InvalidCredentialsException) {
            assertThat(e).isInstanceOf(InvalidCredentialsException::class.java)
        }
    }

    @Test
    fun `getUserByName should return user when found`() = runBlocking {
        // Given
        coEvery { remoteDataSource.getUserByUserName(user1.username) } returns userDto
        every { mapper.dtoToUser(userDto) } returns user1

        // When
        val result = repository.getUserByName(user1.username)

        // Then
        assertThat(result).isEqualTo(user1)
    }

    @Test
    fun `getUserByName should return null when user not found`() = runBlocking {
        // Given
        coEvery { remoteDataSource.getUserByUserName(user1.username) } returns null

        // When
        val result = repository.getUserByName(user1.username)

        // Then
        assertThat(result).isNull()
    }

    @Test
    fun `createUser should map user to dto and call remote data source`() = runBlocking {
        // Given
        every { mapper.userToDto(user1) } returns userDto
        coEvery { remoteDataSource.createUser(userDto) } just Runs

        // When
        repository.createUser(user1)

        // Then
        coVerify(exactly = 1) { remoteDataSource.createUser(userDto) }
    }

    @Test
    fun `saveLoggedUser should call data source with given user`() {
        // When
        repository.saveLoggedUser(user1)

        // Then
        verify { loggedUserDataSource.saveLoggedUser(user1) }
    }

    @Test
    fun `getLoggedUser should return user from data source`() {
        // Given
        every { loggedUserDataSource.getLoggedUser() } returns user1

        // When
        val result = repository.getLoggedUser()

        // Then
        assertThat(result).isEqualTo(user1)
    }
}