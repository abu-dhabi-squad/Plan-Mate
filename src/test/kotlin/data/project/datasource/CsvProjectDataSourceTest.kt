package data.project.datasource

import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
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

    @Test
    fun `readProjects should return list of projects when read file returns list`(){
        //given
        val resState = listOf(
            State("1","state1"),
            State("2","state2"),
            State("3","state3")
        )
        val res = Project("1","name1", resState)
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
        every { fileHelper.writeFile<Project>(any(),any()) } throws FileDoesNotExistException()
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

}