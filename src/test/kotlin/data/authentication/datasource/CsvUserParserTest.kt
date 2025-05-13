package data.authentication.datasource

import com.google.common.truth.Truth.assertThat
import data.authentication.datasource.csv.CsvUserParser
import logic.exceptions.CanNotParseUserException
import logic.model.User
import logic.model.UserType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID

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
            "shahd",
            "password123",
            UserType.MATE
        )
        val result = csvUserParser.parseUserToString(user)
        assertThat(result).isEqualTo("123e4567-e89b-12d3-a456-426614174000,shahd,password123,MATE")
    }

    @Test
    fun `parseStringToUser should return User from valid string`() {
        val line = "123e4567-e89b-12d3-a456-426614174000,shahd,password123,MATE"
        val user = csvUserParser.parseStringToUser(line)
        assertThat(user.userId).isEqualTo(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
        assertThat(user.username).isEqualTo("shahd")
        assertThat(user.password).isEqualTo("password123")
        assertThat(user.userType).isEqualTo(UserType.MATE)
    }

    @Test
    fun `parseStringToUser should throw exception if input is invalid`() {
        // Given
        val invalidLine = "missing,fields"
        // When && Then
        assertThrows<CanNotParseUserException>() {
            csvUserParser.parseStringToUser(invalidLine)
        }
    }
}