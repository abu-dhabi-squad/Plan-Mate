package data.authentication.repository

import com.google.common.truth.Truth.assertThat
import data.authentication.mapper.UserMapper
import data.authentication.model.UserDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.Runs
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import logic.model.User
import logic.model.User.UserType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class AuthenticationRepositoryImplTest {

    private val remoteDataSource: RemoteAuthenticationDataSource = mockk()
    private val loggedUserDataSource: LoggedUserDataSource = mockk()
    private val userMapper: UserMapper = mockk()

    private lateinit var repository: AuthenticationRepositoryImpl

    private val testUser =
        User(username = "noor", password = "password123", userType = UserType.MATE)

    @BeforeEach
    fun setup() {
        repository = AuthenticationRepositoryImpl(
            remoteDataSource,
            loggedUserDataSource,
            userMapper
        )
    }

    @Test
    fun `loginUser should return user when credentials are valid`() = runTest {
        // Given
        val dto = mockk<UserDto>()
        coEvery { remoteDataSource.getUserByUsername("noor") } returns dto
        every { userMapper.dtoToUser(dto) } returns testUser

        // When
        val result = repository.getUserByName("noor")

        // Then
        assertThat(result).isEqualTo(testUser)
    }

    @Test
    fun `loginUser should return null when user does not exist`() = runTest {
        // Given
        coEvery { remoteDataSource.getUserByUsername("noor") } returns null

        // When
        val result = repository.getUserByName("noor")

        // Then
        assertThat(result).isNull()
    }

    @Test
    fun `getUserByName should return user when user exists`() = runTest {
        // Given
        val dto = mockk<UserDto>()
        coEvery { remoteDataSource.getUserByUsername("noor") } returns dto
        every { userMapper.dtoToUser(dto) } returns testUser

        // When
        val result = repository.getUserByName("noor")

        // Then
        assertThat(result).isEqualTo(testUser)
    }

    @Test
    fun `getUserByName should return null when user does not exist`() = runTest {
        // Given
        coEvery { remoteDataSource.getUserByUsername("noor") } returns null

        // When
        val result = repository.getUserByName("noor")

        // Then
        assertThat(result).isNull()
    }

    @Test
    fun `saveLoggedUser should save user as-is without hashing`() {
        //Given
        every { loggedUserDataSource.saveLoggedUser(any()) } just Runs
        //when
        repository.saveLoggedUser(testUser)
        //Then
        verify {
            loggedUserDataSource.saveLoggedUser(
                match {
                    it.username == testUser.username &&
                            it.password == testUser.password &&
                            it.userType == testUser.userType
                }
            )
        }
    }


    @Test
    fun `getLoggedUser should return stored user`() {
        // Given
        every { loggedUserDataSource.getLoggedUser() } returns testUser

        // When
        val result = repository.getLoggedUser()

        // Then
        assertThat(result).isEqualTo(testUser)
    }

    @Test
    fun `createUser should map user to dto and call remote data source`() = runTest {
        // Given
        val userDto = mockk<UserDto>()
        every { userMapper.userToDto(testUser) } returns userDto
        coEvery { remoteDataSource.createUser(userDto) } just Runs

        // When
        repository.createUser(testUser)

        // Then
        coVerify { remoteDataSource.createUser(userDto) }
    }

}
