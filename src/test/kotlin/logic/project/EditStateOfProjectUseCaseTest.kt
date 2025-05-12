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
    private lateinit var editStateToProjectUseCase: EditStateOfProjectUseCase
    private val projectRepository: ProjectRepository = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        editStateToProjectUseCase = EditStateOfProjectUseCase(projectRepository)
    }

    @Test
    fun `editStateOfProject should throw ProjectNotFoundException when projectRepository getProjectById returns null`() = runTest{

        //given
        val newTaskState = TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "newState1")
        val taskStates = listOf(TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1b"), "state1"), TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1d"), "state2"))
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1c"), "name1", taskStates)
        coEvery { projectRepository.getProjectById(any()) } returns null
        //when & then
        assertThrows<ProjectNotFoundException> {
            editStateToProjectUseCase(project.projectId, newTaskState)
        }
    }

    @Test
    fun `editStateOfProject should throw Exception when projectRepository getProjectById throw Exception`() = runTest{
        //given
        val newTaskState = TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "newState1")
        val taskStates = listOf(TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1b"), "state1"), TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1d"), "state2"))
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1c"), "name1", taskStates)
        coEvery { projectRepository.getProjectById(any()) } throws Exception()
        //when & then
        assertThrows<Exception> {
            editStateToProjectUseCase(project.projectId, newTaskState)
        }
    }

    @Test
    fun `editStateOfProject should throw ProjectStateNotFoundException when the new state id not in project's states`() = runTest{
        //given
        val newTaskState = TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "newState1")
        val taskStates = listOf(TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1b"), "state1"), TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1d"), "state2"))
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1c"), "name1", taskStates)
        coEvery { projectRepository.getProjectById(any()) } returns project
        //when & then
        assertThrows<ProjectStateNotFoundException> {
            editStateToProjectUseCase(project.projectId, newTaskState)
        }
    }

    @Test
    fun `editStateOfProject should throw Exception when the projectRepository editProject throw Exception`() = runTest{
        //given
        val newTaskState = TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "newState1")
        val taskStates = listOf(TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "state1"), TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1d"), "state2"))
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1c"), "name1", taskStates)
        coEvery { projectRepository.getProjectById(any()) } returns project
        coEvery { projectRepository.editProject(any()) } throws Exception()
        //when & then
        assertThrows<Exception> {
            editStateToProjectUseCase(project.projectId, newTaskState)
        }
    }

    @Test
    fun `editStateOfProject should call projectRepository editProject function when the state id is found`()= runTest {
        //given
        val newTaskState = TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "newState1")
        val taskStates = listOf(TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "state1"), TaskState(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "state2"))
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1c"), "name1", taskStates)
        coEvery { projectRepository.getProjectById(any()) } returns project
        //when
        editStateToProjectUseCase(project.projectId, newTaskState)
        //then
        coVerify(exactly = 1) { projectRepository.editProject(any()) }
    }
}