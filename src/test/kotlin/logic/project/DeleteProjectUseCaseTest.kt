package logic.project

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
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

        // When
        val result = deleteProjectUseCase.execute(projectId)

        // Then
        assertThat(result).isTrue()
        verify { projectRepository.deleteProject(projectId)}
    }

    @Test
    fun `given non-existent project ID, should return false`() {
        // Given
        val projectId = "invalid-id"
        every { projectRepository.deleteProject(projectId) } returns false

        // When
        val result = deleteProjectUseCase.execute(projectId)

        // Then
        assertThat(result).isFalse()
    }
}