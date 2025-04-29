package data.project.repository

import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import squad.abudhabi.data.project.datasource.ProjectDataSource
import squad.abudhabi.data.project.repository.ProjectRepositoryImpl
import squad.abudhabi.logic.model.Project

class ProjectRepositoryImplTest{

    private val projectDataSource: ProjectDataSource = mockk(relaxed = true)
    private lateinit var projectRepositoryImpl: ProjectRepositoryImpl

    @BeforeEach
    fun setup(){
         projectRepositoryImpl = ProjectRepositoryImpl(projectDataSource)
    }

    @Test
    fun `getProjects should throw Exception when projectDataSource readProjects throw Exception`(){
        //given
        every { projectDataSource.readProjects() } throws Exception()
        //when & then
        assertThrows<Exception>{
            projectRepositoryImpl.getProjects()
        }
    }

    @Test
    fun `getProjects should return empty when projectDataSource readProjects return empty list`(){
        //given
        every { projectDataSource.readProjects() } returns listOf()
        //when & then
        Truth.assertThat(projectRepositoryImpl.getProjects()).isEmpty()
    }

    @Test
    fun `getProjects should return list of projects when projectDataSource readProjects return list of projects`(){
        //given
        val list = listOf(Project("id1","name1", listOf()))
        every { projectDataSource.readProjects() } returns list
        //when & then
        Truth.assertThat(projectRepositoryImpl.getProjects()).isEqualTo(list)
    }

    @Test
    fun `addProject should throw Exception when projectDataSource writeProject throw Exception`(){
        //given
        val res = Project("1","name1", listOf())
        every { projectDataSource.writeProject(any()) } throws Exception()
        //when & then
        assertThrows<Exception>{
            projectRepositoryImpl.addProject(res)
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `addProject should returns like projectDataSource writeProject when it return true or false`(returnValue : Boolean){
        //given
        val res = Project("1","name1", listOf())
        every { projectDataSource.writeProject(any()) } returns returnValue
        //when & then
        Truth.assertThat(projectRepositoryImpl.addProject(res)).isEqualTo(returnValue)
    }

    @Test
    fun `editProject should throw Exception when projectDataSource editProject throw Exception`(){
        //given
        val res = Project("1","name1", listOf())
        every { projectDataSource.editProject(any()) } throws Exception()
        //when & then
        assertThrows<Exception>{
            projectRepositoryImpl.editProject(res)
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `editProject should returns like projectDataSource editProject when it return true or false`(returnValue : Boolean){
        //given
        val res = Project("1","name1", listOf())
        every { projectDataSource.editProject(any()) } returns returnValue
        //when & then
        Truth.assertThat(projectRepositoryImpl.editProject(res)).isEqualTo(returnValue)
    }


    @Test
    fun `deleteProject should throw Exception when projectDataSource deleteProject throw Exception`(){
        //given
        val res = Project("1","name1", listOf())
        every { projectDataSource.deleteProject(any()) } throws Exception()
        //when & then
        assertThrows<Exception>{
            projectRepositoryImpl.deleteProject(res)
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `deleteProject should returns like projectDataSource deleteProject when it return true or false`(returnValue : Boolean){
        //given
        val res = Project("1","name1", listOf())
        every { projectDataSource.deleteProject(any()) } returns returnValue
        //when & then
        Truth.assertThat(projectRepositoryImpl.deleteProject(res)).isEqualTo(returnValue)
    }
}