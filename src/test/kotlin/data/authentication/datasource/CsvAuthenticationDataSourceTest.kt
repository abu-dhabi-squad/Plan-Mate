import com.google.common.truth.Truth
import data.TestData.user1
import data.TestData.user2
import data.TestData.userString1
import data.TestData.userString2
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import data.authentication.datasource.csv_datasource.CsvAuthenticationDataSource
import data.authentication.datasource.csv_datasource.CsvUserParser
import kotlinx.coroutines.test.runTest
import data.utils.filehelper.FileHelper
import logic.exceptions.CanNotParseUserException
import logic.model.User

class CsvAuthenticationDataSourceTest {

    private lateinit var fileHelper: FileHelper
    private lateinit var csvUserParser: CsvUserParser
    private lateinit var csvAuthenticationDataSource: CsvAuthenticationDataSource
    private val filePath = "users.csv"

    @BeforeEach
    fun setup() {
        fileHelper = mockk(relaxed = true)
        csvUserParser = mockk(relaxed = true)
        csvAuthenticationDataSource = CsvAuthenticationDataSource(csvUserParser, fileHelper, filePath)
    }

    @Test
    fun `getUserByUserName should return user when username exists`() = runTest {
        // Given
        every { fileHelper.readFile(filePath) } returns listOf(userString1)
        every { csvUserParser.parseStringToUser(userString1) } returns user1

        // When
        val user = csvAuthenticationDataSource.getUserByUserName("user1")

        // Then
        Truth.assertThat(user).isEqualTo(user1)
}

    @Test
    fun `getUserByUserName should return null when username does not exist`() = runTest {
        // Given
        every { fileHelper.readFile(filePath) } returns listOf(userString1)
        every { csvUserParser.parseStringToUser(userString1) } returns user1

        // When
        val user = csvAuthenticationDataSource.getUserByUserName("user2")

        // Then
        Truth.assertThat(user).isNull()
    }

    @Test
    fun `getAllUsers should return a list of users when readFile returns valid data`() = runTest {
        // Given
        every { fileHelper.readFile(filePath) } returns listOf(userString1)
        every { csvUserParser.parseStringToUser(userString1) } returns user1

        // When
        val users = csvAuthenticationDataSource.getAllUsers()

        // Then
        Truth.assertThat(users).containsExactly(user1)
    }

    @Test
    fun `getAllUsers should return empty list when readFile returns empty list`() = runTest {
        // Given
        every { fileHelper.readFile(any()) } returns emptyList()

        // When & Then
        Truth.assertThat(csvAuthenticationDataSource.getAllUsers()).isEqualTo(emptyList<User>())
    }

    @Test
    fun `getAllUsers should throw Exception when readFile throws Exception`() = runTest {
        // Given
        every { fileHelper.readFile(filePath) } throws Exception()

        // When & Then
        assertThrows<Exception> {
            csvAuthenticationDataSource.getAllUsers()
        }
    }

    @Test
    fun `getAllUsers should throw Exception when parseStringToUser throws Exception`() = runTest {
        // Given
        every { fileHelper.readFile(filePath) } returns listOf(userString1)
        every { csvUserParser.parseStringToUser(any()) } throws CanNotParseUserException()

        // When & Then
        assertThrows<CanNotParseUserException> {
            csvAuthenticationDataSource.getAllUsers()
        }
    }

    @Test
    fun `createUser should save new user when file is empty`() = runTest {
        // Given
        every { fileHelper.readFile(filePath) } returns emptyList()
        every { csvUserParser.parseUserToString(user2) } returns userString2
        every { fileHelper.appendFile(filePath, listOf(userString2)) } just Runs

        // When
        csvAuthenticationDataSource.createUser(user2)

        // Then
        verify(exactly = 1) { fileHelper.appendFile(filePath, listOf(userString2)) }
    }


}