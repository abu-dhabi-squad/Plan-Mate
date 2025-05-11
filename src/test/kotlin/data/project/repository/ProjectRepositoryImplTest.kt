package data.project.repository

import data.project.mapper.ProjectMapper
import data.project.model.ProjectDto
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import logic.model.Project
import kotlinx.coroutines.test.runTest
import logic.repository.ProjectRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

class ProjectRepositoryImplTest {

    private lateinit var projectDataSource: RemoteProjectDataSource
    private lateinit var mapper: ProjectMapper
    private lateinit var repository: ProjectRepository

    private val project = Project(UUID.randomUUID(), "Test", emptyList())
    private val projectDto = ProjectDto(UUID.randomUUID().toString(), "Test", emptyList())

    @BeforeEach
    fun setUp() {
        projectDataSource = mockk()
        mapper = mockk()
        repository = ProjectRepositoryImpl(projectDataSource, mapper)
    }

    @Test
    fun `getAllProjects should return mapped list`() = runTest {
        coEvery { projectDataSource.getAllProjects() } returns listOf(projectDto)
        every { mapper.dtoToProject(projectDto) } returns project

        val result = repository.getAllProjects()

        assert(result == listOf(project))
        coVerify { projectDataSource.getAllProjects() }
    }

    @Test
    fun `addProject should map and call createProject`() = runTest {
        every { mapper.projectToDto(project) } returns projectDto
        coEvery { projectDataSource.createProject(projectDto) } just Runs

        repository.addProject(project)

        coVerify { projectDataSource.createProject(projectDto) }
    }

    @Test
    fun `editProject should map and call editProject`() = runTest {
        every { mapper.projectToDto(project) } returns projectDto
        coEvery { projectDataSource.editProject(projectDto) } just Runs

        repository.editProject(project)

        coVerify { projectDataSource.editProject(projectDto) }
    }

    @Test
    fun `deleteProjectById should call data source`() = runTest {
        val projectId = "d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"
        coEvery { projectDataSource.deleteProject(projectId) } just Runs

        repository.deleteProjectById(projectId)

        coVerify { projectDataSource.deleteProject(projectId) }
    }

    @Test
    fun `getProjectById should return mapped project`() = runTest {
        val projectId = "d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"
        coEvery { projectDataSource.getProjectById(projectId) } returns projectDto
        every { mapper.dtoToProject(projectDto) } returns project

        val result = repository.getProjectById(projectId)

        assert(result == project)
        coVerify { projectDataSource.getProjectById(projectId) }
    }

    @Test
    fun `getProjectById should return null when not found`() = runTest {
        val projectId = "d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"
        coEvery { projectDataSource.getProjectById(projectId) } returns null

        val result = repository.getProjectById(projectId)

        assert(result == null)
    }
}
