package data.utils.filehelper

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.io.TempDir
import squad.abudhabi.data.utils.filehelper.CsvFileHelper
import java.io.File
import java.nio.file.Path

class CsvFileHelperTest {
    private lateinit var csvFileHelper: CsvFileHelper
    private lateinit var fileName: String
    private lateinit var file: File

    @BeforeEach
    fun setUp(@TempDir tempDir: Path) {
        fileName = "$tempDir/test.csv"
        file = File(fileName)
        file.createNewFile()
        csvFileHelper = CsvFileHelper()
    }

    @Test
    fun `readFile should return list of lines when file contains data`() {
        // Given
        val data = listOf("line1", "line2", "line3")
        file.writeText(data.joinToString("\n"))
        // When
        val result = csvFileHelper.readFile(fileName)
        val writtenContent = file.readLines()
        // Then
        assertThat(result).isEqualTo(data)
        assertThat(writtenContent).isEqualTo(data)
    }

    @Test
    fun `readFile should return empty list when file is empty`() {
        // When
        val result = csvFileHelper.readFile(fileName)
        // Then
        assertThat(result).isEmpty()
    }



    @Test
    fun `writeFile should write to the file when data is valid`() {
        // Given
        val data = listOf("line1", "line2", "line3")
        // When
         csvFileHelper.writeFile(fileName, data)
        // Then
        assertThat(file.readLines()).isEqualTo(data)
    }

    @Test
    fun `writeFile should throw IllegalArgumentException when list data is empty`() {
        // Given
        val data = emptyList<String>()
        // When && then
        assertThrows<IllegalArgumentException> {
            csvFileHelper.writeFile(fileName, data)
        }
    }


    @Test
    fun `appendFile should append data to the file when data is valid`() {
        // Given
        val data = listOf("line1", "line2", "line3")
        // When
         csvFileHelper.appendFile(fileName, data)
        // Then
        assertThat(file.readLines()).isEqualTo(data)
    }

    @Test
    fun `appendFile should throw IllegalArgumentException when list data is empty`() {
        // Given
        val data = emptyList<String>()
        // When && then
        assertThrows<IllegalArgumentException> {
            csvFileHelper.appendFile(fileName, data)
        }
    }


}