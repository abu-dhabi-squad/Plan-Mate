package data.project.datasource

import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
 import org.junit.jupiter.params.provider.ValueSource
import squad.abudhabi.data.Exceptions.CanNotParseProjectException
import squad.abudhabi.data.Exceptions.CanNotParseStateException
import squad.abudhabi.data.Exceptions.FileDoesNotExistException
import squad.abudhabi.data.Exceptions.NoProjectsFoundException
import squad.abudhabi.data.project.datasource.CsvProjectDataSource
import squad.abudhabi.data.utils.filehelper.FileHelper
import squad.abudhabi.logic.model.Project
import squad.abudhabi.logic.model.State

class CsvProjectDataSourceTest()
{
    private val fileHelper: FileHelper = mockk()
    private lateinit var csvProjectDataSource: CsvProjectDataSource

    @BeforeEach
    fun setup(){
        csvProjectDataSource = CsvProjectDataSource(fileHelper)
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
    fun `readProjects should throw NoProjectsFoundException when read file returns empty list`(){
        //given
        every { fileHelper.readFile(any()) } returns listOf()
        //when & then
        assertThrows<NoProjectsFoundException> {
            csvProjectDataSource.readProjects()
        }
    }

    @ParameterizedTest
    @ValueSource(strings = [
        "1,name1,ee,1-state1|2-state2|3-state3",
        "1",
    ]
    )
    fun `readProjects should throw CanNotParseProject when line can not be parsed into project object`(project_line : String){
        //given
        every { fileHelper.readFile(any()) } returns listOf(project_line)
        //when & then
        assertThrows<CanNotParseProjectException> {
            csvProjectDataSource.readProjects()
        }
    }

    @ParameterizedTest
    @ValueSource(strings = [
        "1,name1,1-state1|2-state2|3-r-state3",
        "1,name1,1-r-state1",
        "1,name1,1state1",
        "1,name1,1state1|2-state2|3-r-state3",
    ]
    )
    fun `readProjects should throw CanNotParseStateException when line can not be parsed into state object`(state_line: String){
        //given
        every { fileHelper.readFile(any()) } returns listOf(state_line)
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
    fun `writeProjects should throw NoProjectsFoundException when passing an empty list`(){
        //given & when & then
        assertThrows<NoProjectsFoundException> {
            csvProjectDataSource.writeProjects(listOf())
        }
    }

    @Test
    fun `writeProjects should retrun true when list is not empty and write file returns true`(){
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
    fun `writeProjects should retrun false when list is not empty and write file returns false`(){
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