package data.authentication.datasource

import com.google.common.truth.Truth.assertThat
import data.authentication.datasource.csv.CsvAuthentication
import data.authentication.datasource.csv.CsvUserParser
import data.utils.filehelper.FileHelper
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import logic.model.User
import logic.model.User.UserType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class CsvAuthenticationTest {

    private lateinit var csvUserParser: CsvUserParser
    private lateinit var fileHelper: FileHelper
    private lateinit var dataSource: CsvAuthentication

    private val filePath = "test.csv"

    @BeforeEach
    fun setUp() {
        csvUserParser = mockk()
        fileHelper = mockk()
        dataSource = CsvAuthentication(csvUserParser, fileHelper, filePath)
    }

    @Test
    fun `getAllUsers should return list of users from file`() {
        val rawLines = listOf("line1", "line2")
        val parsedUsers = listOf(
            User(Uuid.parse("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "shahd", "password123", UserType.MATE),
            User(Uuid.parse("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "shahdd", "password123", UserType.MATE)
        )

        every { fileHelper.readFile(filePath) } returns rawLines
        every { csvUserParser.parseStringToUser("line1") } returns parsedUsers[0]
        every { csvUserParser.parseStringToUser("line2") } returns parsedUsers[1]

        val result = dataSource.getAllUsers()
        assertThat(result).isEqualTo(parsedUsers)
        verify { fileHelper.readFile(filePath) }
        verify { csvUserParser.parseStringToUser("line1") }
        verify { csvUserParser.parseStringToUser("line2") }
    }

    @Test
    fun `getUserByUserName should return user if found`() {
        val rawLines = listOf("line1", "line2")
        val existingUser = User(Uuid.parse("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "shahd", "password123", UserType.MATE)

        val otherUser = User(Uuid.parse("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "shahdd", "password123", UserType.MATE)


        every { fileHelper.readFile(filePath) } returns rawLines
        every { csvUserParser.parseStringToUser("line1") } returns otherUser
        every { csvUserParser.parseStringToUser("line2") } returns existingUser

        val result = dataSource.getUserByUserName("shahd")
        assertThat(result).isEqualTo(existingUser)
    }

    @Test
    fun `getUserByUserName should return null if user not found`() {
        val rawLines = listOf("line1")
        val user = User(Uuid.parse("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "shahd", "password123", UserType.MATE)

        every { fileHelper.readFile(filePath) } returns rawLines
        every { csvUserParser.parseStringToUser("line1") } returns user

        val result = dataSource.getUserByUserName("mohamed")
        assertThat(result).isNull()
    }

    @Test
    fun `createUser should save user to file`() {
        val existingUsers = listOf("line1")
        val existingUser = User(Uuid.parse("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "shahd", "password123", UserType.MATE)

        val newUser = User(Uuid.parse("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1b"), "shahd", "password123", UserType.MATE)


        every { fileHelper.readFile(filePath) } returns existingUsers
        every { csvUserParser.parseStringToUser("line1") } returns existingUser
        every { csvUserParser.parseUserToString(existingUser) } returns "line1"
        every { csvUserParser.parseUserToString(newUser) } returns "line2"
        every { fileHelper.appendFile(filePath, listOf("line1", "line2")) } just Runs

        dataSource.createUser(newUser)

        verify {
            fileHelper.appendFile(filePath, listOf("line1", "line2"))
        }
    }
}
