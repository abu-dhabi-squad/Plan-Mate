package data.authentication.repository

import com.google.common.truth.Truth
import data.TestData.user1
import data.TestData.user2
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import squad.abudhabi.data.authentication.datasource.AuthenticationDataSource
import squad.abudhabi.data.authentication.repository.AuthenticationRepositoryImpl
import squad.abudhabi.logic.exceptions.InvalidCredentialsException
import squad.abudhabi.logic.exceptions.UserAlreadyExistsException
import squad.abudhabi.logic.exceptions.UserNotFoundException


class AuthenticationRepositoryImplTest {


    private lateinit var authenticationDataSource: AuthenticationDataSource
    private lateinit var authenticationRepository: AuthenticationRepositoryImpl

    @BeforeEach
    fun setup() {
        authenticationDataSource = mockk()
        authenticationRepository = AuthenticationRepositoryImpl(authenticationDataSource)
    }

    @Test
    fun `login should return user when username and password are correct`() {
        // Given
        every { authenticationDataSource.getUserByUserName(user1.username) } returns user1

        // When
        val result = authenticationRepository.login(user1.username, "pass1")

        // Then
        Truth.assertThat(result).isEqualTo(user1)
    }

    @Test
    fun `login should throw InvalidCredentialsException when password is incorrect`() {
        // Given
        every { authenticationDataSource.getUserByUserName(user1.username) } returns user1

        // When & Then
        assertThrows<InvalidCredentialsException> {
            authenticationRepository.login(user1.username, "wrongPassword")
        }
    }

    @Test
    fun `login should throw InvalidCredentialsException when password is empty`() {
        every { authenticationDataSource.getUserByUserName(user1.username) } returns user1

        assertThrows<InvalidCredentialsException> {
            authenticationRepository.login(user1.username, "")
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

}
