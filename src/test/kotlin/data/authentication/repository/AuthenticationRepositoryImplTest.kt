package data.authentication.repository

import com.google.common.truth.Truth.assertThat
import data.authentication.model.UserDto
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import logic.exceptions.InvalidCredentialsException
import logic.model.User
import logic.model.UserType
import presentation.logic.utils.hashing.HashingService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class AuthenticationRepositoryImplTest {

    private val remoteDataSource: RemoteAuthenticationDataSource = mockk()
    private val loggedUserDataSource: LoggedUserDataSource = mockk()
    private val hashingService: HashingService = mockk()
    private val mongoUserMapper: data.authentication.mapper.MongoUserMapper = mockk()

    private lateinit var repository: AuthenticationRepositoryImpl

    private val testUser =
        User(username = "noor", password = "password123", userType = UserType.MATE)

    @BeforeEach
    fun setup() {
        repository = AuthenticationRepositoryImpl(
            remoteDataSource,
            loggedUserDataSource,
            hashingService,
            mongoUserMapper
        )
    }

    @Test
    fun `loginUser should return user when credentials are valid`() = runTest {
        //Given
        val hashedPassword = "hashedPassword"
        val dto = mockk<UserDto> {
            every { password } returns hashedPassword
        }
        every { hashingService.hash("password123") } returns hashedPassword
        coEvery { remoteDataSource.getUserByUsername("noor") } returns dto
        every { mongoUserMapper.dtoToUser(dto) } returns testUser
        // When
        val result = repository.loginUser("noor", "password123")
        // Then
        assertThat(testUser).isEqualTo(result)
    }

    @Test
    fun `loginUser should throw InvalidCredentialsException when password is incorrect`() =
        runTest {
            // Given
            val hashedPassword = "hashedPassword"
            val wrongPassword = "wrongHashed"

            val dto = mockk<UserDto> {
                every { password } returns wrongPassword
            }

            every { hashingService.hash("password123") } returns hashedPassword
            coEvery { remoteDataSource.getUserByUsername("john") } returns dto
            // When && Then
            assertThrows<InvalidCredentialsException> {
                repository.loginUser("john", "password123")
            }
        }

    @Test
    fun `getUserByName should return user when user is exists`() = runTest {
        // Given
        val dto = mockk<UserDto>()
        coEvery { remoteDataSource.getUserByUsername("noor") } returns dto
        every { mongoUserMapper.dtoToUser(dto) } returns testUser
        // When
        val result = repository.getUserByName("noor")
        // Then
        assertThat(testUser).isEqualTo(result)
    }

    @Test
    fun `getUserByName should return null when user is not exists`() = runTest {
        // Given
        coEvery { remoteDataSource.getUserByUsername("noor") } returns null
        // When
        val result = repository.getUserByName("noor")
        // Then
        assertThat(result).isNull()
    }

    @Test
    fun `createUser should hash password and call data source`() = runTest {
        //Given
        val hashedPassword = "hashedPassword"
        val userDto = mockk<UserDto>()
        val userWithHashedPassword = testUser.copy(password = hashedPassword)
        every { hashingService.hash("password123") } returns hashedPassword
        every { mongoUserMapper.userToDto(userWithHashedPassword) } returns userDto
        coEvery { remoteDataSource.createUser(userDto) } just Runs
        // When
        repository.createUser(testUser)
        // Then
        coVerify { remoteDataSource.createUser(userDto) }
    }

    @Test
    fun `saveLoggedUser should hash password and save user`() {
        // Given
        val hashedPassword = "hashedPassword"

        every { hashingService.hash("password123") } returns hashedPassword
        every { loggedUserDataSource.saveLoggedUser(any()) } just Runs
        // When
        repository.saveLoggedUser(testUser)
        // Then
        verify {
            loggedUserDataSource.saveLoggedUser(
                match { it.username == testUser.username && it.password == hashedPassword }
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
        assertThat(testUser).isEqualTo(result)
    }
}