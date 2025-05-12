package logic.project

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.exceptions.ProjectNotFoundException
import logic.exceptions.ProjectStateNotFoundException
import logic.model.Project
import logic.model.TaskState
import logic.repository.ProjectRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID

class EditStateOfProjectUseCaseTest {
    private lateinit var editStateOfProjectUseCase: EditStateOfProjectUseCase
    private val projectRepository: ProjectRepository = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        editStateOfProjectUseCase = EditStateOfProjectUseCase(projectRepository)
    }

    @Test
    fun `editStateOfProject should throw ProjectNotFoundException when projectRepository getProjectById returns null`() = runTest{

        //Given
        val newTaskState = TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "newState1")
        val taskStates = listOf(TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1b"), "state1"), TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1d"), "state2"))
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1c"), "name1", taskStates)
        coEvery { projectRepository.getProjectById(any()) } returns null
        //When & Then
        assertThrows<ProjectNotFoundException> {
            editStateOfProjectUseCase(project.projectId, newTaskState)
        }
    }

    @Test
    fun `editStateOfProject should throw Exception when projectRepository getProjectById throw Exception`() = runTest{
        //Given
        val newTaskState = TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "newState1")
        val taskStates = listOf(TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1b"), "state1"), TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1d"), "state2"))
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1c"), "name1", taskStates)
        coEvery { projectRepository.getProjectById(any()) } throws Exception()
        //When & Then
        assertThrows<Exception> {
            editStateOfProjectUseCase(project.projectId, newTaskState)
        }
    }

    @Test
    fun `editStateOfProject should throw ProjectStateNotFoundException when the new state id not in project's states`() = runTest{
        //Given
        val newTaskState = TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "newState1")
        val taskStates = listOf(TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1b"), "state1"), TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1d"), "state2"))
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1c"), "name1", taskStates)
        coEvery { projectRepository.getProjectById(any()) } returns project
        //When & Then
        assertThrows<ProjectStateNotFoundException> {
            editStateOfProjectUseCase(project.projectId, newTaskState)
        }
    }

    @Test
    fun `editStateOfProject should throw Exception when the projectRepository editProject throw Exception`() = runTest{
        //Given
        val newTaskState = TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "newState1")
        val taskStates = listOf(TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "state1"), TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1d"), "state2"))
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1c"), "name1", taskStates)
        coEvery { projectRepository.getProjectById(any()) } returns project
        coEvery { projectRepository.editProject(any()) } throws Exception()
        //When & Then
        assertThrows<Exception> {
            editStateOfProjectUseCase(project.projectId, newTaskState)
        }
    }

    @Test
    fun `editStateOfProject should call projectRepository editProject function when the state id is found`()= runTest {
        //Given
        val newTaskState = TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "newState1")
        val taskStates = listOf(TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "state1"), TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "state2"))
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1c"), "name1", taskStates)
        coEvery { projectRepository.getProjectById(any()) } returns project
        //When
        editStateOfProjectUseCase(project.projectId, newTaskState)
        //Then
        coVerify(exactly = 1) { projectRepository.editProject(any()) }
    }
}