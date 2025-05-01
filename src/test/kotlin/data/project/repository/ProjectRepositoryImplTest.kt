package data.project.repository

import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import squad.abudhabi.data.project.datasource.ProjectDataSource
import squad.abudhabi.data.project.repository.ProjectRepositoryImpl
import squad.abudhabi.logic.model.Project
import squad.abudhabi.logic.model.State

class ProjectRepositoryImplTest {

    private val projectDataSource: ProjectDataSource = mockk(relaxed = true)
    private lateinit var projectRepositoryImpl: ProjectRepositoryImpl

    @BeforeEach
    fun setup() {
        projectRepositoryImpl = ProjectRepositoryImpl(projectDataSource)
    }

    @Test
    fun `getProjects should throw Exception when projectDataSource readProjects throw Exception`() {
        //given
        every { projectDataSource.getAllProjects() } throws Exception()
        //when & then
        assertThrows<Exception> {
            projectRepositoryImpl.getAllProjects()
        }
    }

    @Test
    fun `getProjects should return empty when projectDataSource readProjects return empty list`() {
        //given
        every { projectDataSource.getAllProjects() } returns listOf()
        //when & then
        Truth.assertThat(projectRepositoryImpl.getAllProjects()).isEmpty()
    }

    @Test
    fun `getProjects should return list of projects when projectDataSource readProjects return list of projects`() {
        //given
        val list = listOf(Project("id1", "name1", listOf()))
        every { projectDataSource.getAllProjects() } returns list
        //when & then
        Truth.assertThat(projectRepositoryImpl.getAllProjects()).isEqualTo(list)
    }

    @Test
    fun `addProject should throw Exception when projectDataSource writeProject throw Exception`() {
        //given
        val res = Project("1", "name1", listOf())
        every { projectDataSource.createProject(any()) } throws Exception()
        //when & then
        assertThrows<Exception> {
            projectRepositoryImpl.addProject(res)
        }
    }

    @Test
    fun `addProject should be successful when no error occur (best case scenario)`(){
        //given
        val res = Project("1", "name1", listOf())
        //when
        projectRepositoryImpl.addProject(res)
        //then
        verify(exactly = 1) { projectDataSource.createProject(res) }
    }

    @Test
    fun `editProject should throw Exception when projectDataSource editProject throw Exception`() {
        //given
        val res = Project("1", "name1", listOf())
        every { projectDataSource.editProject(any()) } throws Exception()
        //when & then
        assertThrows<Exception> {
            projectRepositoryImpl.editProject(res)
        }
    }

    @Test
    fun `editProject should be successful when no error occur (best case scenario)`() {
        //given
        val res = Project("1", "name1", listOf())
        //when
        projectRepositoryImpl.editProject(res)
        //then
        verify(exactly = 1) { projectDataSource.editProject(res) }
    }

    @Test
    fun `deleteProject should throw Exception when projectDataSource deleteProject throw Exception`() {
        //given
        val res = "1"
        every { projectDataSource.deleteProject(any()) } throws Exception()
        //when & then
        assertThrows<Exception> {
            projectRepositoryImpl.deleteProject(res)
        }
    }

    @Test
    fun `deleteProject should be successful when no error occur (best case scenario)`() {
        //given
        val res = "1"
        //when
        projectRepositoryImpl.deleteProject(res)
        //when & then
        verify(exactly = 1) { projectDataSource.deleteProject(res) }
    }

    @Test
    fun `getProjectById should throw Exception when projectDataSource getProject throw Exception`() {
        //given
        every { projectDataSource.getProject(any()) } throws Exception()
        //when & then
        assertThrows<Exception> {
            projectRepositoryImpl.getProjectById("1")
        }
    }

    @Test
    fun `getProjectById should return null when projectDataSource getProject return null`() {
        //given
        every { projectDataSource.getProject(any()) } returns null
        //when & then
        Truth.assertThat(projectRepositoryImpl.getProjectById("1")).isNull()
    }

    @Test
    fun `getProjectById should return project when projectDataSource getProject return project`() {
        //given
        val res = Project("1", "name1", listOf(State("1", "name1")))
        every { projectDataSource.getProject(any()) } returns res
        //when & then
        Truth.assertThat(projectRepositoryImpl.getProjectById("1")).isEqualTo(res)
    }
}