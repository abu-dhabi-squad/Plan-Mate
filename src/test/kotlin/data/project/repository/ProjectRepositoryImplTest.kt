package data.project.repository

import com.google.common.truth.Truth.assertThat
import com.mongodb.MongoException
import data.project.datasource.mongo_datasource.RemoteProjectDataSource
import data.project.mapper.ProjectMapper
import data.project.model.ProjectDto
import io.mockk.*
import kotlinx.coroutines.test.runTest
import logic.model.Project
import logic.model.State
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class ProjectRepositoryImplTest {
    private lateinit var remoteDataSource: RemoteProjectDataSource
    private lateinit var projectMapper: ProjectMapper
    private lateinit var repository: ProjectRepositoryImpl

    @BeforeEach
    fun setup() {
        remoteDataSource = mockk()
        projectMapper = mockk()
        repository = ProjectRepositoryImpl(remoteDataSource, projectMapper)
    }

    @Test
    fun `getAllProjects should return empty list when remote returns empty`() = runTest {
        coEvery { remoteDataSource.getAllProjects() } returns emptyList()
        coEvery { projectMapper.projectDtoToProject(any()) } returns mockk()

        val result = repository.getAllProjects()

        verify (exactly=0) { projectMapper.projectDtoToProject(any()) }
        assertThat(result).isEmpty()
    }

    @Test
    fun `getAllProjects should return mapped projects`() = runTest {
        val dto1 = mockk<ProjectDto>()
        val dto2 = mockk<ProjectDto>()
        val project1 = Project(UUID.randomUUID(), "Project 1", emptyList())
        val project2 = Project(UUID.randomUUID(), "Project 2", emptyList())

        coEvery { remoteDataSource.getAllProjects() } returns listOf(dto1, dto2)
        coEvery { projectMapper.projectDtoToProject(dto1) } returns project1
        coEvery { projectMapper.projectDtoToProject(dto2) } returns project2

        val result = repository.getAllProjects()

        assertThat(result).containsExactly(project1, project2).inOrder()
    }

    @Test
    fun `getAllProjects should throw when remote throws`() = runTest {
        coEvery { remoteDataSource.getAllProjects() } throws Exception("Network error")

        assertThrows<Exception> {
            repository.getAllProjects()
        }
    }


    @Test
    fun `getAllProjects should throw when mapper throws`() = runTest {
        val dto1 = mockk<ProjectDto>()
        val dto2 = mockk<ProjectDto>()
        coEvery { remoteDataSource.getAllProjects() } returns listOf(dto1, dto2)
        coEvery { projectMapper.projectDtoToProject(any()) } throws Exception()

        assertThrows<Exception> {
            repository.getAllProjects()
        }
    }

    @Test
    fun `addProject should map and forward to remote`() = runTest {
        val project = Project(UUID.randomUUID(), "New Project", emptyList())
        val dto = mockk<ProjectDto>()

        coEvery { projectMapper.projectToProjectDto(project) } returns dto
        coEvery { remoteDataSource.createProject(dto) } returns Unit

        repository.addProject(project)

        coVerify {
            projectMapper.projectToProjectDto(project)
            remoteDataSource.createProject(dto)
        }
    }

    @Test
    fun `editProject should map and forward to remote`() = runTest {
        val project = Project(UUID.randomUUID(), "Updated Project", emptyList())
        val dto = mockk<ProjectDto>()

        coEvery { projectMapper.projectToProjectDto(project) } returns dto
        coEvery { remoteDataSource.editProject(dto) } returns Unit

        repository.editProject(project)

        coVerify {
            projectMapper.projectToProjectDto(project)
            remoteDataSource.editProject(dto)
        }
    }

    @Test
    fun `deleteProject should forward to remote`() = runTest {
        val projectId = "123"
        coEvery { remoteDataSource.deleteProject(projectId) } returns Unit

        repository.deleteProject(projectId)

        coVerify { remoteDataSource.deleteProject(projectId) }
    }

    @Test
    fun `getProjectById should return null when remote return null`() = runTest {
        val projectId = "123"
        coEvery { remoteDataSource.getProjectById(projectId) } returns null

        val result = repository.getProjectById(projectId)

        assertThat(result).isNull()
    }


    @Test
    fun `getProjectById should return mapped project`() = runTest {
        val projectId = "123"
        val dto = mockk<ProjectDto>()
        val project = Project(UUID.randomUUID(), "Project", listOf(State("1", "Todo")))

        coEvery { remoteDataSource.getProjectById(projectId) } returns dto
        coEvery { projectMapper.projectDtoToProject(dto) } returns project

        val result = repository.getProjectById(projectId)

        assertThat(result).isEqualTo(project)
    }


    @Test
    fun `getAllProjects should throw MongoException when remote throws MongoException`() = runTest {
        coEvery { remoteDataSource.getAllProjects() } throws MongoException("Database error")
        assertThrows<MongoException> {
            repository.getAllProjects()
        }
    }

}

