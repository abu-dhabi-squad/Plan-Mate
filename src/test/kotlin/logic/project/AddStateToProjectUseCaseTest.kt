package logic.project

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import logic.exceptions.DuplicateStateException
import logic.exceptions.ProjectNotFoundException
import logic.model.Project
import logic.model.TaskState
import logic.repository.ProjectRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class AddStateToProjectUseCaseTest {
    private lateinit var projectRepository: ProjectRepository
    private lateinit var addStateToProjectUseCase: AddStateToProjectUseCase

    @BeforeEach
    fun setup() {
        projectRepository = mockk(relaxed = true)
        addStateToProjectUseCase = AddStateToProjectUseCase(projectRepository)
    }

    @Test
    fun `should add new state to existing project`() = runTest{
        // Given
        val existingTaskState = TaskState(stateId = Uuid.parse("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1b"), stateName = "TODO")
        val newTaskState = TaskState(stateId = Uuid.parse("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1b"), stateName = "InProgress")
        val existingProject = Project(projectId = Uuid.parse("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), projectName = "Test Project", taskStates = listOf(existingTaskState))

        coEvery { projectRepository.getProjectById(Uuid.parse("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a")) } returns existingProject

        // When
        addStateToProjectUseCase(Uuid.parse("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), newTaskState)

        // Then
        coVerify {
            projectRepository.editProject(
                match {
                    it.projectId == Uuid.parse("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a") && it.taskStates.contains(newTaskState)
                }
            )
        }
    }

    @Test
    fun `should throw DuplicateStateException when state name already exists`() = runTest{
        // Given
        val existingTaskState = TaskState(stateId = Uuid.parse("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), stateName = "TODO")
        val duplicateTaskState = TaskState(stateId = Uuid.parse("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), stateName = "TODO")
        val existingProject = Project(projectId = Uuid.parse("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1b"), projectName = "Test Project", taskStates = listOf(existingTaskState))

        coEvery { projectRepository.getProjectById(any()) } returns existingProject

        // When & Then
        val exception = assertThrows<DuplicateStateException> {
            addStateToProjectUseCase.invoke(Uuid.parse("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), duplicateTaskState)
        }
        assertThat(exception).hasMessageThat().contains("TODO")
    }

    @Test
    fun `should throw Project Not FoundException when project id is invalid`()= runTest {
        // Given
        coEvery { projectRepository.getProjectById(any()) } returns null
        val newTaskState = TaskState(stateId = Uuid.parse("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), stateName = "Review")

        // When & Then
        assertThrows<ProjectNotFoundException> {
            addStateToProjectUseCase.invoke(Uuid.parse("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), newTaskState)
        }
    }

    @Test
    fun `should throw DuplicateStateException when state name matches existing one ignoring case`() = runTest{
        // Given
        val existingTaskState = TaskState(stateId = Uuid.parse("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), stateName = "ToDo")
        val duplicateTaskState = TaskState(stateId = Uuid.parse("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1b"), stateName = "todo") // same name, different case
        val existingProject = Project(projectId = Uuid.parse("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1d"), projectName = "Test Project", taskStates = listOf(existingTaskState))

        coEvery { projectRepository.getProjectById(any()) } returns existingProject

        // When & Then
        val exception = assertThrows<DuplicateStateException> {
            addStateToProjectUseCase.invoke(Uuid.parse("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), duplicateTaskState)
        }

        assertThat(exception).hasMessageThat().contains("todo")
    }

    @Test
    fun `should handle adding first state to project with empty states list`() = runTest{
        // Given
        val newTaskState = TaskState(stateId = Uuid.parse("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), stateName = "TODO")
        val existingProject = Project(projectId = Uuid.parse("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1b"), projectName = "Test Project", taskStates = emptyList())

        coEvery { projectRepository.getProjectById(any()) } returns existingProject

        // When
        addStateToProjectUseCase.invoke(Uuid.parse("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1b"), newTaskState)

        // Then
        coVerify {
            projectRepository.editProject(
                match {
                    it.projectId == Uuid.parse("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1b") && it.taskStates.size == 1 && it.taskStates[0] == newTaskState
                }
            )
        }
    }
}