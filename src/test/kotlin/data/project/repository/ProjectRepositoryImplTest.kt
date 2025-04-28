package data.project.repository

import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import squad.abudhabi.data.Exceptions.CanNotParseProjectException
import squad.abudhabi.data.Exceptions.CanNotParseStateException
import squad.abudhabi.data.Exceptions.FileDoesNotExistException
import squad.abudhabi.data.Exceptions.NoProjectsFoundException
import squad.abudhabi.data.project.datasource.ProjectDataSource
import squad.abudhabi.data.project.repository.ProjectRepositoryImpl
import squad.abudhabi.logic.model.Project
import squad.abudhabi.logic.model.State

class ProjectRepositoryImplTest{

    private val projectDataSource: ProjectDataSource = mockk(relaxed = true)
    private lateinit var projectRepositoryImpl: ProjectRepositoryImpl

    @BeforeEach
    fun setup(){
         projectRepositoryImpl = ProjectRepositoryImpl(projectDataSource)
    }

    @Test
    fun `getProjects should throw FileDoesNotExistException when readProjects throw FileDoesNotExistException`(){
        //given
        every { projectDataSource.readProjects() } throws FileDoesNotExistException()
        //when & then
        assertThrows<FileDoesNotExistException>{
            projectRepositoryImpl.getProjects()
        }
    }

    @Test
    fun `getProjects should throw NoProjectsFoundException when readProjects throw NoProjectsFoundException`(){
        //given
        every { projectDataSource.readProjects() } throws NoProjectsFoundException()
        //when & then
        assertThrows<NoProjectsFoundException>{
            projectRepositoryImpl.getProjects()
        }
    }

    @Test
    fun `getProjects should throw CanNotParseProjectException when readProjects throw CanNotParseProjectException`(){
        //given
        every { projectDataSource.readProjects() } throws CanNotParseProjectException()
        //when & then
        assertThrows<CanNotParseProjectException>{
            projectRepositoryImpl.getProjects()
        }
    }

    @Test
    fun `getProjects should throw CanNotParseStateException when readProjects throw CanNotParseStateException`(){
        //given
        every { projectDataSource.readProjects() } throws CanNotParseStateException()
        //when & then
        assertThrows<CanNotParseStateException>{
            projectRepositoryImpl.getProjects()
        }
    }

    @Test
    fun `getProjects should throw NoProjectsFoundException when readProjects return empty list`(){
        //given
        val resState = listOf(
            State("1","state1"),
            State("2","state2"),
            State("3","state3")
        )
        val res = Project("1","name1", resState)
        every { projectDataSource.readProjects() } returns listOf()
        //when & then
        assertThrows<NoProjectsFoundException> {
            projectRepositoryImpl.getProjects()
        }
    }

    @Test
    fun `addProject should throw FileDoesNotExistException when writeProjects throw FileDoesNotExistException`(){
        //given
        val resState = listOf(
            State("1","state1"),
            State("2","state2"),
            State("3","state3")
        )
        val res = Project("1","name1", resState)
        every { projectDataSource.writeProjects(any()) } throws FileDoesNotExistException()
        //when & then
        assertThrows<FileDoesNotExistException>{
            projectRepositoryImpl.addProject(res)
        }
    }
}