package data.project.repository

import com.google.common.truth.Truth.assertThat
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
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class ProjectRepositoryImplTest {

    private lateinit var projectDataSource: RemoteProjectDataSource
    private lateinit var mapper: ProjectMapper
    private lateinit var repository: ProjectRepository

    private val project = Project(Uuid.random(), "Test", emptyList())
    private val projectDto = ProjectDto(Uuid.random().toString(), "Test", emptyList())

    @BeforeEach
    fun setUp() {
        projectDataSource = mockk()
        mapper = mockk()
        repository = ProjectRepositoryImpl(projectDataSource, mapper)
    }

    @Test
    fun `getAllProjects should return mapped list`() = runTest {
        // Given
        coEvery { projectDataSource.getAllProjects() } returns listOf(projectDto)
        every { mapper.dtoToProject(projectDto) } returns project

        // When
        val result = repository.getAllProjects()

        // Then
        assertThat(result).isEqualTo(listOf(project))
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
        val projectId = Uuid.parse("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a")
        coEvery { projectDataSource.deleteProject(projectId.toString()) } just Runs

        repository.deleteProjectById(projectId)

        coVerify { projectDataSource.deleteProject(projectId.toString()) }
    }

    @Test
    fun `getProjectById should return mapped project`() = runTest {
        // Given
        val projectId = Uuid.parse("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a")
        coEvery { projectDataSource.getProjectById(projectId.toString()) } returns projectDto
        every { mapper.dtoToProject(projectDto) } returns project

        // When
        val result = repository.getProjectById(projectId)

        // Then
        assertThat(result).isEqualTo(project)
        coVerify { projectDataSource.getProjectById(projectId.toString()) }
    }



    @Test
    fun `getProjectById should return null when not found`() = runTest {
        // Given
        val projectId = Uuid.parse("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a")
        coEvery { projectDataSource.getProjectById(projectId.toString()) } returns null

        // When
        val result = repository.getProjectById(projectId)

        // Then
        assertThat(result).isNull()
    }

}
