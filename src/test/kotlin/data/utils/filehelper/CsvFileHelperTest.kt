package data.utils.filehelper

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.io.TempDir
import squad.abudhabi.data.utils.filehelper.CsvFileHelper
import java.io.File
import java.io.FileNotFoundException
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
        //given
        val data = listOf("line1", "line2", "line3")
        file.writeText(data.joinToString("\n"))
        //when
        val result = csvFileHelper.readFile(fileName)
        val writtenContent = file.readLines()
        //then
        assertThat(result).isEqualTo(data)
        assertThat(writtenContent).isEqualTo(data)
    }

    @Test
    fun `readFile should return empty list when file is empty`() {
        //when
        val result = csvFileHelper.readFile(fileName)
        //then
        assertThat(result).isEmpty()
    }

    @Test
    fun `readFile should throw FileNotFoundException when file does not exist`() {
        //given
        file.delete()
        //when && then
        assertThrows<FileNotFoundException> {
            csvFileHelper.readFile(fileName)
        }
    }

    @Test
    fun `writeFile should write to the file when data is valid`() {
        //given
        val data = listOf("line1", "line2", "line3")
        //when
         csvFileHelper.writeFile(fileName, data)
        //then
        assertThat(file.readLines()).isEqualTo(data)
    }

    @Test
    fun `writeFile should throw IllegalArgumentException when list data is empty`() {
        //given
        val data = emptyList<String>()
        //when && then
        assertThrows<IllegalArgumentException> {
            csvFileHelper.writeFile(fileName, data)
        }
    }


    @Test
    fun `appendFile should append data to the file when data is valid`() {
        //given
        val data = listOf("line1", "line2", "line3")
        //when
         csvFileHelper.appendFile(fileName, data)
        //then
        assertThat(file.readLines()).isEqualTo(data)
    }

    @Test
    fun `appendFile should throw IllegalArgumentException when list data is empty`() {
        //given
        val data = emptyList<String>()
        //when && then
        assertThrows<IllegalArgumentException> {
            csvFileHelper.appendFile(fileName, data)
        }
    }


}