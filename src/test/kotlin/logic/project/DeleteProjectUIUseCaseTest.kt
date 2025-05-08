package logic.project


import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import logic.exceptions.ProjectNotFoundException
import logic.model.Project
import logic.repository.ProjectRepository

class DeleteProjectUIUseCaseTest {

    private lateinit var projectRepository: ProjectRepository
    private lateinit var deleteProjectUseCase: DeleteProjectUseCase

    @BeforeEach
    fun setup() {
        projectRepository = mockk(relaxed = true)
        deleteProjectUseCase = DeleteProjectUseCase(projectRepository)
    }

    @Test
    fun `given valid project ID, should delete project successfully`() {
        // Given
        val projectId = "Test Project"
        every { projectRepository.getProjectById(any()) } returns Project(projectId, "test", emptyList())
        // When
        deleteProjectUseCase.invoke(projectId)

        // Then
        verify { projectRepository.deleteProject(projectId) }
    }

    @Test
    fun `return false when delete project return false`() {
        // Given
        val projectId = "invalid-id"
        every { projectRepository.deleteProject(projectId) } throws Exception()
        every { projectRepository.getProjectById(any()) } returns Project(projectId, "test", emptyList())

        // When & Then
        assertThrows<Exception> {
            deleteProjectUseCase.invoke(projectId)
        }
    }

    @Test
    fun `should throw exception when data is not exist`() {
        // Given
        val projectId = "invalid-id"
        every { projectRepository.getProjectById(any()) } returns null

        // When & Then
        assertThrows<ProjectNotFoundException> {
            deleteProjectUseCase.invoke(projectId)
        }
        verify(exactly = 0) { projectRepository.deleteProject(any()) }
    }

    @Test
    fun `given non-existent project ID, should throw exception`() {
        // Given
        val projectId = "invalid-id"
        every { projectRepository.getProjectById(any()) } returns null

        // When & Then
        assertThrows<ProjectNotFoundException> {
            deleteProjectUseCase.invoke(projectId)
        }
        verify(exactly = 0) { projectRepository.deleteProject(any()) }
    }

    @Test
    fun `should throw exception when their is an issue in getProject`() {
        // Given
        val projectId = "invalid-id"
        every { projectRepository.getProjectById(any()) } throws Exception()

        // When & Then
        assertThrows<Exception> {
            deleteProjectUseCase.invoke(projectId)
        }
        verify(exactly = 0) { projectRepository.deleteProject(any()) }
    }
}