package data.project.datasource

import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import squad.abudhabi.logic.exceptions.CanNotParseProjectException
import squad.abudhabi.logic.exceptions.CanNotParseStateException
import squad.abudhabi.logic.exceptions.FileDoesNotExistException
import squad.abudhabi.data.project.datasource.CsvProjectDataSource
import squad.abudhabi.data.project.datasource.CsvProjectParser
import squad.abudhabi.data.utils.filehelper.FileHelper
import squad.abudhabi.logic.model.Project
import squad.abudhabi.logic.model.State

class CsvProjectDataSourceTest
{
    private val fileHelper: FileHelper = mockk(relaxed = true)
    private val csvProjectParser: CsvProjectParser = mockk(relaxed = true)
    private lateinit var csvProjectDataSource: CsvProjectDataSource

    @BeforeEach
    fun setup(){
        csvProjectDataSource = CsvProjectDataSource(fileHelper,csvProjectParser)
    }

    @Test
    fun `readProjects should throw FileDoesNotExistException when read file throw FileDoesNotExistException`(){
        //given
        every { fileHelper.readFile(any()) } throws FileDoesNotExistException()
        //when & then
        assertThrows<FileDoesNotExistException> {
            csvProjectDataSource.readProjects()
        }
    }

    @Test
    fun `readProjects should returns emplty list when read file returns empty list`(){
        //given
        every { fileHelper.readFile(any()) } returns listOf()
        //when & then
        Truth.assertThat(csvProjectDataSource.readProjects()).isEqualTo(listOf<Project>())
    }

    @Test
    fun `readProjects should throw CanNotParseProjectException when parser throw CanNotParseProjectException`(){
        //given
        every { fileHelper.readFile(any()) } returns listOf("id1,name1,id1-name1")
        every { csvProjectParser.parseStringToProject(any()) } throws CanNotParseProjectException()
        //when & then
        assertThrows<CanNotParseProjectException> {
            csvProjectDataSource.readProjects()
        }
    }

    @Test
    fun `readProjects should throw CanNotParseStateException when parser throw CanNotParseStateException`(){
        //given
        every { fileHelper.readFile(any()) } returns listOf("id1,name1,id1-name1")
        every { csvProjectParser.parseStringToProject(any()) } throws CanNotParseStateException()
        //when & then
        assertThrows<CanNotParseStateException> {
            csvProjectDataSource.readProjects()
        }
    }

    @Test
    fun `readProjects should return list of projects when read file returns list`(){
        //given
        val resState = listOf(
            State("1","state1"),
            State("2","state2"),
            State("3","state3")
        )
        val res = listOf(Project("1","name1", resState))
        every { fileHelper.readFile(any()) } returns listOf("1,name1,1-state1|2-state2|3-state3")
        every { csvProjectParser.parseStringToProject(any()) } returns Project("1","name1", resState)
        //when & then
        Truth.assertThat(csvProjectDataSource.readProjects()).isEqualTo(res)
    }

    @Test
    fun `writeProjects should throw FileDoesNotExistException when write file throw FileDoesNotExistException`(){
        //given
        val resState = listOf(
            State("1","state1"),
            State("2","state2"),
            State("3","state3")
        )
        val res = Project("1","name1", resState)
        every { fileHelper.writeFile(any(),any()) } throws FileDoesNotExistException()
        //when & then
        assertThrows<FileDoesNotExistException> {
            csvProjectDataSource.writeProjects(listOf(res,res))
        }
    }

    @Test
    fun `writeProjects should return false when passing an empty list`(){
        //given & when & then
        Truth.assertThat(csvProjectDataSource.writeProjects(listOf())).isFalse()
    }

    @Test
    fun `writeProjects should return true when list is not empty and write file returns true`(){
        //given
        val resState = listOf(
            State("1","state1"),
            State("2","state2"),
            State("3","state3")
        )
        val res = Project("1","name1", resState)
        every { fileHelper.writeFile(any(),any()) } returns true
        //when & then
        Truth.assertThat(csvProjectDataSource.writeProjects(listOf(res,res))).isTrue()
    }

    @Test
    fun `writeProjects should return false when list is not empty and write file returns false`(){
        //given
        val resState = listOf(
            State("1","state1"),
            State("2","state2"),
            State("3","state3")
        )
        val res = Project("1","name1", resState)
        every { fileHelper.writeFile(any(),any()) } returns false
        //when & then
        Truth.assertThat(csvProjectDataSource.writeProjects(listOf(res,res))).isFalse()
    }

}