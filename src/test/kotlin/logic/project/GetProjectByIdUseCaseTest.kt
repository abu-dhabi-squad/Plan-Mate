package logic.project

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import squad.abudhabi.logic.exceptions.NoProjectsFoundException
import squad.abudhabi.logic.exceptions.ProjectNotFoundException
import squad.abudhabi.logic.model.Project
import squad.abudhabi.logic.project.GetProjectByIdUseCase
import squad.abudhabi.logic.repository.ProjectRepository
import kotlin.test.Test

class GetProjectByIdUseCaseTest {
    private lateinit var projectRepository: ProjectRepository
    private lateinit var getProjectByIdUseCase: GetProjectByIdUseCase

    @BeforeEach
    fun setUp() {
        projectRepository = mockk()
        getProjectByIdUseCase = GetProjectByIdUseCase(projectRepository)
    }

    @Test
    fun `should return the project when a project with the given id exists`() {
        //Given
        val project = Project(id = "1", projectName = "Project One", states = emptyList())
        every { projectRepository.getProjects() } returns listOf(project)
        // When
        val result = getProjectByIdUseCase("1")
        //Then
        assertThat(result).isEqualTo(project)
    }

    @Test
    fun `should throw NoProjectsFoundException when no projects exist`() {
        // Given
        every { projectRepository.getProjects() } returns emptyList()
        // When & Then
        assertThrows<NoProjectsFoundException> {
            getProjectByIdUseCase("1")
        }
    }

    @Test
    fun `should throw ProjectNotFoundException when a project with the given id does not exist`() {
        // Given
        val projects = listOf(
            Project(id = "2", projectName = "Project One", states = emptyList()),
            Project(id = "3", projectName = "Project One", states = emptyList())
        )
        every { projectRepository.getProjects() } returns projects
        // When & Then
        assertThrows<ProjectNotFoundException> {
            getProjectByIdUseCase("1")
        }
    }

}