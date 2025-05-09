package data.authentication.datasource


import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import data.TestData.user1
import data.TestData.userString1
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import data.authentication.datasource.csv_datasource.CsvUserParser
import logic.exceptions.CanNotParseUserException
import logic.model.User
import logic.model.UserType
import java.util.*

class CsvUserParserTest {
    private lateinit var csvUserParser: CsvUserParser

    @BeforeEach
    fun setup() {
        csvUserParser = CsvUserParser()
    }

    @Test
    fun `parseUserToString should return comma separated string`() {
        val user = User(
            UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
            "john",
            "password123",
            UserType.MATE
        )
        val result = csvUserParser.parseUserToString(user)
        assertThat(result).isEqualTo("123e4567-e89b-12d3-a456-426614174000,john,password123,MATE")
    }

    @Test
    fun `parseStringToUser should return User from valid string`() {
        val line = "123e4567-e89b-12d3-a456-426614174000,john,password123,MATE"
        val user = csvUserParser.parseStringToUser(line)
        assertThat(user.id).isEqualTo(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
        assertThat(user.username).isEqualTo("john")
        assertThat(user.password).isEqualTo("password123")
        assertThat(user.userType).isEqualTo(UserType.MATE)
    }

    @Test
    fun `parseStringToUser should throw exception if input is invalid`() {
        val invalidLine = "missing,fields"
        try {
            csvUserParser.parseStringToUser(invalidLine)
            throw AssertionError("Expected CanNotParseUserException to be thrown")
        } catch (e: CanNotParseUserException) {
            // success
        }
    }

}