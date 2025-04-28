package data.project.repository

import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import squad.abudhabi.data.Exceptions.*
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
    fun `getProjects should throw FileDoesNotExistException when readProjects throw FileDoesNotExistException`(){
        //given
        every { projectDataSource.readProjects() } throws FileDoesNotExistException()
        //when & then
        assertThrows<FileDoesNotExistException>{
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
        every { projectDataSource.readProjects() } returns listOf()
        //when & then
        assertThrows<NoProjectsFoundException> {
            projectRepositoryImpl.getProjects()
        }
    }

    @Test
    fun `addProject should throw FileDoesNotExistException when readProjects throw FileDoesNotExistException`(){
        //given
        val res = Project("1","name1", listOf())
        every { projectDataSource.readProjects() } throws FileDoesNotExistException()
        //when & then
        assertThrows<FileDoesNotExistException>{
            projectRepositoryImpl.addProject(res)
        }
    }

    @Test
    fun `addProject should throw FileDoesNotExistException when writeProjects throw FileDoesNotExistException`(){
        //given
        val res = Project("1","name1", listOf())
        every { projectDataSource.writeProjects(any()) } throws FileDoesNotExistException()
        //when & then
        assertThrows<FileDoesNotExistException>{
            projectRepositoryImpl.addProject(res)
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = [
        true,
        false,
    ]
    )
    fun `addProject should returns like writeProjects when readProjects return empty list`(returnValue : Boolean){
        //given
        val res = Project("1","name1", listOf())
        every { projectDataSource.writeProjects(any()) } returns returnValue
        every { projectDataSource.readProjects() } returns listOf()
        //when & then
        Truth.assertThat(projectRepositoryImpl.addProject(res)).isEqualTo(returnValue)
    }

    @ParameterizedTest
    @ValueSource(booleans = [
        true,
        false,
    ]
    )
    fun `addProject should returns like writeProjects when readProjects return list of values`(returnValue : Boolean){
        //given
        val res = Project("1","name1", listOf())
        every { projectDataSource.writeProjects(any()) } returns returnValue
        every { projectDataSource.readProjects() } returns listOf(res)
        //when & then
        Truth.assertThat(projectRepositoryImpl.addProject(res)).isEqualTo(returnValue)
    }

    @Test
    fun `editProject should throw FileDoesNotExistException when readProjects throw FileDoesNotExistException`(){
        //given
        val res = Project("1","name1", listOf())
        every { projectDataSource.readProjects() } throws FileDoesNotExistException()
        //when & then
        assertThrows<FileDoesNotExistException>{
            projectRepositoryImpl.editProject(res)
        }
    }

    @Test
    fun `editProject should throw Exception when readProjects throw Exception`(){
        //given
        val res = Project("1","name1", listOf())
        every { projectDataSource.readProjects() } throws Exception()
        //when & then
        assertThrows<Exception>{
            projectRepositoryImpl.editProject(res)
        }
    }

    @Test
    fun `editProject should throw Exception when readProjects returns empty list`(){
        //given
        val res = Project("1","name1", listOf())
        every { projectDataSource.readProjects() } returns listOf()
        //when & then
        assertThrows<NoProjectsFoundException>{
            projectRepositoryImpl.editProject(res)
        }
    }

    @Test
    fun `editProject should throw ProjectNotInListException when project id not in data`(){
        //given
        val data = listOf(
            Project("2","name2", listOf()),
            Project("3","name3", listOf()),
            Project("4","name4", listOf()),
        )
        val input = Project("1","name11", listOf())
        every { projectDataSource.readProjects() } returns data
        //when & then
        assertThrows<ProjectNotInListException>{
            projectRepositoryImpl.editProject(input)
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = [
        true,
        false,
    ]
    )
    fun `editProject should return like writeProject when project id is in the data`(returnValue: Boolean){
        //given
        val data = listOf(
            Project("1","name1", listOf()),
            Project("2","name2", listOf()),
            Project("3","name3", listOf()),
            Project("4","name4", listOf()),
        )
        val input = Project("1","name11", listOf())
        every { projectDataSource.readProjects() } returns data
        every { projectDataSource.writeProjects(any()) } returns returnValue
        //when & then
        Truth.assertThat(projectRepositoryImpl.editProject(input)).isEqualTo(returnValue)
    }
}