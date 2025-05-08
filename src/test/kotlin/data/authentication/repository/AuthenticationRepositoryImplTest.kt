package data.authentication.repository

import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.just
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import data.authentication.datasource.localdatasource.LoggedUserDataSource
import data.authentication.datasource.mongo_datasource.RemoteAuthenticationDataSource
import data.authentication.mapper.UserMapper
import data.authentication.model.UserDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import logic.exceptions.InvalidCredentialsException
import logic.model.User
import logic.model.UserType

class AuthenticationRepositoryImplTest {
    private lateinit var remoteDataSource: RemoteAuthenticationDataSource
    private lateinit var loggedUserDataSource: LoggedUserDataSource
    private lateinit var userMapper: UserMapper
    private lateinit var repository: AuthenticationRepositoryImpl

    private lateinit var user: User
    private lateinit var userDto: UserDto

    @BeforeEach
    fun setup() {
        remoteDataSource = mockk(relaxed = true)
        loggedUserDataSource = mockk(relaxed = true)
        userMapper = mockk(relaxed = true)
        repository = AuthenticationRepositoryImpl(remoteDataSource, loggedUserDataSource, userMapper)

        user = User(
            id = "1",
            username = "noor",
            password = "secret123",
            userType = UserType.MATE
        )

        userDto = UserDto(
            id = "1",
            username = "noor",
            password = "secret123",
            userType = "MATE"
        )
    }

    @Test
    fun `loginUser should return user when username and password are correct`() = runTest {
        // Given
        coEvery { remoteDataSource.getUserByUserName(user.username) } returns userDto
        every { userMapper.dtoToUser(userDto) } returns user

        // When
        val result = repository.loginUser(user.username, "secret123")

        // Then
        assertThat(result).isEqualTo(user)
    }

    @Test
    fun `loginUser should throw InvalidCredentialsException when password does not match`() = runTest {
        // Given
        coEvery { remoteDataSource.getUserByUserName(user.username) } returns userDto.copy(password = "wrongpass")

        // When & Then
        assertThrows<InvalidCredentialsException> {
            repository.loginUser(user.username, "secret123")
        }
    }

    @Test
    fun `loginUser should throw InvalidCredentialsException when user is not found`() = runTest {
        // Given
        coEvery { remoteDataSource.getUserByUserName(user.username) } returns null

        // When & Then
        assertThrows<InvalidCredentialsException> {
            repository.loginUser(user.username, "secret123")
        }
    }

    @Test
    fun `getUserByName should return mapped user if user exists`() = runTest {
        coEvery { remoteDataSource.getUserByUserName(user.username) } returns userDto
        every { userMapper.dtoToUser(userDto) } returns user

        val result = repository.getUserByName(user.username)

        assertThat(result).isEqualTo(user)
    }

    @Test
    fun `getUserByName should return null if user not found`() = runTest {
        coEvery { remoteDataSource.getUserByUserName(user.username) } returns null

        val result = repository.getUserByName(user.username)

        assertThat(result).isNull()
    }

    @Test
    fun `createUser should pass mapped UserDto to remote data source`() = runTest {
        val mappedDto = userDto
        every { userMapper.userToDto(user) } returns mappedDto
        coEvery { remoteDataSource.createUser(mappedDto) } just Runs

        repository.createUser(user)

        coVerify { remoteDataSource.createUser(mappedDto) }
    }

    @Test
    fun `saveLoggedUser should forward call to loggedUserDataSource`() {
        repository.saveLoggedUser(user)

        verify { loggedUserDataSource.saveLoggedUser(user) }
    }

    @Test
    fun `getLoggedUser should return the stored user`() {
        every { loggedUserDataSource.getLoggedUser() } returns user

        val result = repository.getLoggedUser()

        assertThat(result).isEqualTo(user)
    }

    @Test
    fun `getLoggedUser should return null when no user is stored`() {
        every { loggedUserDataSource.getLoggedUser() } returns null

        val result = repository.getLoggedUser()

        assertThat(result).isNull()
    }
}