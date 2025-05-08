//import com.google.common.truth.Truth.assertThat
//import data.TestData.user1
//import data.TestData.user2
//import data.authentication.repository.AuthenticationRepositoryImpl
//import io.mockk.Runs
//import io.mockk.just
//import io.mockk.mockk
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.assertThrows
//import data.authentication.datasource.localdatasource.LoggedUserDataSource
//import data.authentication.datasource.mongo_datasource.RemoteAuthenticationDataSource
//import io.mockk.coEvery
//import io.mockk.coVerify
//import kotlinx.coroutines.test.runTest
//import logic.exceptions.InvalidCredentialsException
//import logic.model.User
//import logic.model.UserType
//class AuthenticationRepositoryImplTest {
//    private lateinit var authenticationDataSource: RemoteAuthenticationDataSource
//    private lateinit var authenticationRepository: AuthenticationRepositoryImpl
//    private lateinit var loggedUserDataSource: LoggedUserDataSource
//    @BeforeEach
//    fun setup() {
//        authenticationDataSource = mockk(relaxed = true)
//        loggedUserDataSource=mockk(relaxed = true)
//        authenticationRepository = AuthenticationRepositoryImpl(authenticationDataSource,loggedUserDataSource)
//    }
//    @Test
//    fun `login should return user when username and password are correct`() = runTest {
//        // Given
//        coEvery { authenticationDataSource.getUserByUserName(user1.username) } returns user1
//        // When
//        val result = authenticationRepository.loginUser(user1.username, "pass1")
//        // Then
//        assertThat(result).isEqualTo(user1)
//    }
//    @Test
//    fun `login should throw InvalidCredentialsException when password is incorrect`() = runTest {
//        // Given
//        coEvery { authenticationDataSource.getUserByUserName(user1.username) } returns user1
//        // When & Then
//        assertThrows<InvalidCredentialsException> {
//            authenticationRepository.loginUser(user1.username, "wrongPassword")
//        }
//    }
//    @Test
//    fun `login should throw InvalidCredentialsException when password is empty`() = runTest {
//        coEvery { authenticationDataSource.getUserByUserName(user1.username) } returns user1
//        assertThrows<InvalidCredentialsException> {
//            authenticationRepository.loginUser(user1.username, "")
//        }
//    }
//    @Test
//    fun `getUserByName should return user when username exists`() = runTest {
//        // Given
//        coEvery { authenticationDataSource.getUserByUserName(user1.username) } returns user1
//        // When
//        val result = authenticationRepository.getUserByName(user1.username)
//        // Then
//        assertThat(result).isEqualTo(user1)
//    }
//    @Test
//    fun `createUser should create user when username does not exist`() = runTest {
//        // Given
//        coEvery { authenticationDataSource.getUserByUserName(user2.username) } returns null
//        coEvery { authenticationDataSource.createUser(user2) } just Runs
//        // When
//        authenticationRepository.createUser(user2)
//        // Then
//        coVerify(exactly = 1) { authenticationDataSource.createUser(user2) }
//    }
//    @Test
//    fun `createUser should check if the user already exists before adding`() = runTest {
//        // Given
//        coEvery { authenticationDataSource.getUserByUserName(user2.username) } returns null
//        coEvery { authenticationDataSource.createUser(user2) } just Runs
//        // When
//        authenticationRepository.createUser(user2)
//        // Then
//        coVerify(exactly = 1) { authenticationDataSource.createUser(user2) }
//    }
//    @Test
//    fun `login should throw InvalidCredentialsException when user is not found`() = runTest {
//        // Given
//        coEvery { authenticationDataSource.getUserByUserName(user1.username) } returns null
//        // When & Then
//        assertThrows<InvalidCredentialsException> {
//            authenticationRepository.loginUser(user1.username, user1.password)
//        }
//    }
//    @Test
//    fun `saveLoggedUser should save user correctly`() = runTest {
//        val user = User(
//            username = "",
//            password = "ValidPass123!",
//            userType = UserType.MATE
//        )
//        coEvery { loggedUserDataSource.getLoggedUser() } returns user
//        authenticationRepository.saveLoggedUser(user)
//        assertThat(authenticationRepository.getLoggedUser()).isEqualTo(user)
//    }
//    @Test
//    fun `getLoggedUser should return null when no user was logged`() = runTest {
//        coEvery { loggedUserDataSource.getLoggedUser() } returns null
//        val res=authenticationRepository.getLoggedUser()
//        assertThat(res).isNull()
//    }
//}