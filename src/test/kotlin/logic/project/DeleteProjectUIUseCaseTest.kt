package logic.project


import io.mockk.*
import kotlinx.coroutines.test.runTest
import logic.exceptions.ProjectNotFoundException
import logic.model.Project
import logic.repository.ProjectRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class DeleteProjectUIUseCaseTest {

    private lateinit var projectRepository: ProjectRepository
    private lateinit var deleteProjectUseCase: DeleteProjectUseCase

    @BeforeEach
    fun setup() {
        projectRepository = mockk(relaxed = true)
        deleteProjectUseCase = DeleteProjectUseCase(projectRepository)
    }

    @Test
    fun `given valid project ID, should delete project successfully`() = runTest{
        // Given
        val projectId = UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a")
        coEvery { projectRepository.getProjectById(any()) } returns Project(projectId, "test", emptyList())
        // When
        deleteProjectUseCase.invoke(projectId.toString())

        // Then
        coVerify { projectRepository.deleteProjectById(projectId.toString()) }
    }

    @Test
    fun `return false when delete project return false`() = runTest{
        // Given
        val projectId = UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a")
        coEvery { projectRepository.deleteProjectById(projectId.toString()) } throws Exception()
        coEvery { projectRepository.getProjectById(any()) } returns Project(projectId, "test", emptyList())

        // When & Then
        assertThrows<Exception> {
            deleteProjectUseCase.invoke(projectId.toString())
        }
    }

    @Test
    fun `should throw exception when data is not exist`()= runTest {
        // Given
        val projectId = UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a")
        coEvery { projectRepository.getProjectById(any()) } returns null

        // When & Then
        assertThrows<ProjectNotFoundException> {
            deleteProjectUseCase.invoke(projectId.toString())
        }
        coVerify(exactly = 0) { projectRepository.deleteProjectById(any()) }
    }

    @Test
    fun `given non-existent project ID, should throw exception`() = runTest{
        // Given
        val projectId = "invalid-id"
        coEvery { projectRepository.getProjectById(any()) } returns null

        // When & Then
        assertThrows<ProjectNotFoundException> {
            deleteProjectUseCase.invoke(projectId)
        }
        coVerify(exactly = 0) { projectRepository.deleteProjectById(any()) }
    }

    @Test
    fun `should throw exception when their is an issue in getProject`() = runTest{
        // Given
        val projectId = "invalid-id"
        coEvery { projectRepository.getProjectById(any()) } throws Exception()

        // When & Then
        assertThrows<Exception> {
            deleteProjectUseCase.invoke(projectId)
        }
        coVerify(exactly = 0) { projectRepository.deleteProjectById(any()) }
    }
}