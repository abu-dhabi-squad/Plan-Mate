package logic.project

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import squad.abudhabi.logic.exceptions.ProjectNotFoundException
import squad.abudhabi.logic.model.Project
import squad.abudhabi.logic.project.DeleteProjectUseCase
import squad.abudhabi.logic.repository.ProjectRepository

class DeleteProjectUseCaseTest {

    private lateinit var projectRepository: ProjectRepository
    private lateinit var deleteProjectUseCase: DeleteProjectUseCase

    @BeforeEach
    fun setup() {
        projectRepository = mockk()
        deleteProjectUseCase = DeleteProjectUseCase(projectRepository)
    }

    @Test
    fun `given valid project ID, should delete project successfully`() {
        // Given
        val projectId = "Test Project"
        every { projectRepository.deleteProject(projectId) } returns true
        every { projectRepository.getProjects() } returns listOf(Project(projectId,"test", emptyList()))

        // When
        val result = deleteProjectUseCase.execute(projectId)

        // Then
        assertThat(result).isTrue()
        verify { projectRepository.deleteProject(projectId)}
    }

    @Test
    fun `return false when delete project return false`() {
        // Given
        val projectId = "invalid-id"
        every { projectRepository.deleteProject(projectId) } returns false
        every { projectRepository.getProjects() } returns listOf(Project(projectId,"test", emptyList()))

        // When
        val result = deleteProjectUseCase.execute(projectId)

        // Then
        assertThat(result).isFalse()
    }

    @Test
    fun `should throw exception when data is not exist`() {
        // Given
        val projectId = "invalid-id"
        every { projectRepository.getProjects() } returns emptyList()

        // When & Then
        assertThrows<ProjectNotFoundException>{
            deleteProjectUseCase.execute(projectId)
        }
        verify(exactly = 0) { projectRepository.deleteProject(any()) }
    }

    @Test
    fun `given non-existent project ID, should throw exception`() {
        // Given
        val projectId = "invalid-id"
        every { projectRepository.getProjects() } returns listOf(Project("Test123","TestName", emptyList()))

        // When & Then
        assertThrows<ProjectNotFoundException>{
            deleteProjectUseCase.execute(projectId)
        }
        verify(exactly = 0) { projectRepository.deleteProject(any()) }
    }

    @Test
    fun `should throw exception when their is an issue in getProject`() {
        // Given
        val projectId = "invalid-id"
        every { projectRepository.getProjects() } throws Exception()

        // When & Then
        assertThrows<Exception>{
            deleteProjectUseCase.execute(projectId)
        }
        verify(exactly = 0) { projectRepository.deleteProject(any()) }
    }
}