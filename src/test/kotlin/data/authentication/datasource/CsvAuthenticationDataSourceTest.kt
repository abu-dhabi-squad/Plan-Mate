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
import squad.abudhabi.data.authentication.datasource.CsvAuthenticationDataSource
import squad.abudhabi.data.authentication.datasource.CsvUserParser
import squad.abudhabi.data.utils.filehelper.FileHelper
import squad.abudhabi.logic.exceptions.CanNotParseUserException
import squad.abudhabi.logic.model.User

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
    fun `getAllUsers should return a list of users when readFile returns valid data`() {
        // Given
        every { fileHelper.readFile(filePath) } returns listOf(userString1)
        every { csvUserParser.parseStringToUser(userString1) } returns user1

        // When
        val users = csvAuthenticationDataSource.getAllUsers()

        // Then
        Truth.assertThat(users).containsExactly(user1)
    }

    @Test
    fun `getAllUsers should return empty list when readFile returns empty list`() {
        // Given
        every { fileHelper.readFile(any()) } returns emptyList()

        // When & Then
        Truth.assertThat(csvAuthenticationDataSource.getAllUsers()).isEqualTo(emptyList<User>())
    }

    @Test
    fun `getAllUsers should throw Exception when readFile throws Exception`() {
        // Given
        every { fileHelper.readFile(filePath) } throws Exception()

        // When & Then
        assertThrows<Exception> {
            csvAuthenticationDataSource.getAllUsers()
        }
    }

    @Test
    fun `getAllUsers should throw Exception when parseStringToUser throws Exception`() {
        // Given
        every { fileHelper.readFile(filePath) } returns listOf(userString1)
        every { csvUserParser.parseStringToUser(any()) } throws CanNotParseUserException()

        // When & Then
        assertThrows<CanNotParseUserException> {
            csvAuthenticationDataSource.getAllUsers()
        }
    }

    @Test
    fun `saveUsers should write file when users list is empty`() {
        // Given
        val emptyUsers = listOf<User>()
        every { fileHelper.writeFile(filePath, any()) } just Runs

        // When
        csvAuthenticationDataSource.saveUsers(emptyUsers)

        // Then
        verify(exactly = 1) { fileHelper.writeFile(filePath, emptyList()) }
    }

    @Test
    fun `saveUsers should append to file when users list is not empty`() {
        // Given
        val users = listOf(user1, user2)
        every { fileHelper.appendFile(filePath, any()) } just Runs
        every { csvUserParser.parseUserToString(user1) } returns userString1
        every { csvUserParser.parseUserToString(user2) } returns userString2

        // When
        csvAuthenticationDataSource.saveUsers(users)

        // Then
        verify(exactly = 1) { fileHelper.appendFile(filePath, listOf(userString1, userString2)) }
    }

    @Test
    fun `saveUsers should throw Exception when appendFile throws Exception`() {
        // Given
        val users = listOf(user1, user2)
        every { fileHelper.appendFile(filePath, any()) } throws Exception()

        // When & Then
        assertThrows<Exception> {
            csvAuthenticationDataSource.saveUsers(users)
        }
    }

}
