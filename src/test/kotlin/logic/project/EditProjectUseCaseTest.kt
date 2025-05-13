package logic.project

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.exceptions.ProjectNotFoundException
import logic.model.Project
import logic.model.TaskState
import logic.repository.ProjectRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID

class EditProjectUseCaseTest {
    private lateinit var editProjectUseCase: EditProjectUseCase
    private lateinit var projectRepository: ProjectRepository

    @BeforeEach
    fun setup() {
        projectRepository = mockk(relaxed = true)
        editProjectUseCase = EditProjectUseCase(projectRepository)
    }

    @Test
    fun `should throw ProjectNotFoundException when it returns null`() = runTest{
        //Given
        val taskState = TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "stateName")
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", listOf(taskState))
        val newName = "name2"
        coEvery { projectRepository.getProjectById(any()) } returns null

        //When & Then
        assertThrows<ProjectNotFoundException> {
            editProjectUseCase(project.projectId,newName)
        }
    }

    @Test
    fun `should throw Exception when it failed`() = runTest{
        //Given
        val taskState = TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "stateName")
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", listOf(taskState))
        val newName = "name2"
        coEvery { projectRepository.getProjectById(any()) } throws Exception()

        //When & Then
        assertThrows<Exception> {
            editProjectUseCase(project.projectId,newName)
        }
    }

    @Test
    fun `should throw Exception when the projectRepository editProject throw Exception`() = runTest{
        //Given
        val taskState = TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "stateName")
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", listOf(taskState))
        val newName = "name2"
        coEvery { projectRepository.editProject(any()) } throws Exception()
        coEvery { projectRepository.getProjectById(any()) } returns project

        //When & Then
        assertThrows<Exception> {
            editProjectUseCase(project.projectId,newName)
        }
    }

    @Test
    fun `should call projectRepository editProject function when the id is found`() = runTest{
        //Given
        val taskState = TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "stateName")
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", listOf(taskState))
        val newName = "name2"
        coEvery { projectRepository.getProjectById(any()) } returns project

        //When
        editProjectUseCase(project.projectId,newName)

        //Then
        coVerify (exactly = 1){ projectRepository.editProject(any()) }
    }
}