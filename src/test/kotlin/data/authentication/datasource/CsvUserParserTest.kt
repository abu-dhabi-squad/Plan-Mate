import com.google.common.truth.Truth
import data.TestData.user1
import data.TestData.userString1
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import data.authentication.datasource.CsvUserParser
import logic.exceptions.CanNotParseUserException
import kotlin.test.assertFailsWith

class CsvUserParserTest {
    private lateinit var csvUserParser: CsvUserParser

    @BeforeEach
    fun setup() {
        csvUserParser = CsvUserParser()
    }

    @Test
    fun `parseUserToString should convert User to CSV string correctly`() {
        // Given
        val user = user1

        // When
        val result = csvUserParser.parseUserToString(user)

        // Then
        Truth.assertThat(result).isEqualTo(userString1)
    }

    @Test
    fun `parseStringToUser should convert CSV string to User object correctly`() {
        // Given
        val csvString = userString1

        // When
        val result = csvUserParser.parseStringToUser(csvString)

        // Then
        Truth.assertThat(result).isEqualTo(user1)
    }

    @Test
    fun `parseStringToUser should throw CanNotParseUserException when CSV string has incorrect format`() {
        // Given
        val invalidCsvString = "1,user1,password1"

        // When & Then
        assertFailsWith<CanNotParseUserException> {
            csvUserParser.parseStringToUser(invalidCsvString)
        }
    }

    @Test
    fun `parseStringToUser should throw CanNotParseUserException when CSV string has more than 4 parts`() {
        // Given
        val invalidCsvString = "1,user1,password1,MATE,city"

        // When & Then
        assertFailsWith<CanNotParseUserException> {
            csvUserParser.parseStringToUser(invalidCsvString)
        }
    }

    @Test
    fun `parseStringToUser should throw Exception when argument is invalid`() {
        // Given
        val invalidCsvString = "1,user1,password1,Regular"

        // When & Then
        assertFailsWith<Exception> {
            csvUserParser.parseStringToUser(invalidCsvString)
        }
    }

}