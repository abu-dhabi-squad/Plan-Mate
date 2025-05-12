package logic.project

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.exceptions.ProjectNotFoundException
import logic.model.Project
import logic.repository.ProjectRepository
import logic.repository.TaskRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID

class DeleteProjectUseCaseTest {

    private lateinit var projectRepository: ProjectRepository
    private lateinit var deleteProjectUseCase: DeleteProjectUseCase
    private lateinit var taskRepository: TaskRepository

    @BeforeEach
    fun setup() {
        projectRepository = mockk(relaxed = true)
        taskRepository = mockk(relaxed = true)
        deleteProjectUseCase = DeleteProjectUseCase(projectRepository,taskRepository)
    }

    @Test
    fun `should call deleteProjectById when given valid project ID`() = runTest {
        // Given
        val projectId = UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a")
        coEvery { projectRepository.getProjectById(any()) } returns Project(projectId, "test", emptyList())

        // When
        deleteProjectUseCase.invoke(projectId)

        // Then
        coVerify { projectRepository.deleteProjectById(projectId) }
    }

    @Test
    fun `should call deleteTasksByProjectById when given valid project ID`() = runTest {
        // Given
        val projectId = UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a")
        coEvery { projectRepository.getProjectById(any()) } returns Project(projectId, "test", emptyList())

        // When
        deleteProjectUseCase.invoke(projectId)

        // Then
        coVerify { taskRepository.deleteTasksByProjectById(projectId) }
    }

    @Test
    fun `return false when delete project return false`() = runTest{
        // Given
        val projectId = UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a")
        coEvery { projectRepository.deleteProjectById(projectId) } throws Exception()
        coEvery { projectRepository.getProjectById(any()) } returns Project(projectId, "test", emptyList())

        // When & Then
        assertThrows<Exception> {
            deleteProjectUseCase.invoke(projectId)
        }
    }

    @Test
    fun `should throw exception when data is not exist`()= runTest {
        // Given
        val projectId = UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a")
        coEvery { projectRepository.getProjectById(any()) } returns null

        // When & Then
        assertThrows<ProjectNotFoundException> {
            deleteProjectUseCase.invoke(projectId)
        }
        coVerify(exactly = 0) { projectRepository.deleteProjectById(any()) }
    }

    @Test
    fun `given non-existent project ID, should throw exception`() = runTest{
        // Given
        val projectId = UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a")
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
        val projectId = UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a")
        coEvery { projectRepository.getProjectById(any()) } throws Exception()

        // When & Then
        assertThrows<Exception> {
            deleteProjectUseCase.invoke(projectId)
        }
        coVerify(exactly = 0) { projectRepository.deleteProjectById(any()) }
    }

    @Test
    fun `should throw exception if deleting tasks fails after project deletion`() = runTest {
        // Given
        val projectId = UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a")
        coEvery { projectRepository.getProjectById(any()) } returns Project(projectId, "test", emptyList())
        coEvery { taskRepository.deleteTasksByProjectById(projectId) } throws Exception("Task deletion failed")

        // When & Then
        assertThrows<Exception> {
            deleteProjectUseCase.invoke(projectId)
        }
        coVerify { projectRepository.deleteProjectById(projectId) }
    }
}


