package logic.project

import com.google.common.truth.Truth.assertThat
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*
import logic.exceptions.DuplicateStateException
import logic.exceptions.ProjectNotFoundException
import logic.model.Project
import logic.model.State
import logic.repository.ProjectRepository

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
        val existingState = State(id = "s1", name = "TODO")
        val newState = State(id = "s2", name = "InProgress")
        val existingProject = Project(id = UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), projectName = "Test Project", states = listOf(existingState))

        coEvery { projectRepository.getProjectById("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a") } returns existingProject

        // When
        addStateToProjectUseCase("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a", newState)

        // Then
        coVerify {
            projectRepository.editProject(
                match {
                    it.id == UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a") && it.states.contains(newState)
                }
            )
        }
    }

    @Test
    fun `should throw DuplicateStateException if state name already exists`() = runTest{
        // Given
        val existingState = State(id = "s1", name = "TODO")
        val duplicateState = State(id = "s2", name = "TODO")
        val existingProject = Project(id = UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), projectName = "Test Project", states = listOf(existingState))

        coEvery { projectRepository.getProjectById(any()) } returns existingProject

        // When & Then
        val exception = assertThrows<DuplicateStateException> {
            addStateToProjectUseCase.invoke("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a", duplicateState)
        }
        assertThat(exception).hasMessageThat().contains("TODO")
    }

    @Test
    fun `should throw Project Not FoundException if project id is invalid`()= runTest {
        // Given
        coEvery { projectRepository.getProjectById(any()) } returns null
        val newState = State(id = "s1", name = "Review")

        // When & Then
        assertThrows<ProjectNotFoundException> {
            addStateToProjectUseCase.invoke("invalid_project", newState)
        }
    }

    @Test
    fun `should throw DuplicateStateException if state name matches existing one ignoring case`() = runTest{
        // Given
        val existingState = State(id = "s1", name = "ToDo")
        val duplicateState = State(id = "s2", name = "todo") // same name, different case
        val existingProject = Project(id = UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), projectName = "Test Project", states = listOf(existingState))

        coEvery { projectRepository.getProjectById(any()) } returns existingProject

        // When & Then
        val exception = assertThrows<DuplicateStateException> {
            addStateToProjectUseCase.invoke("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a", duplicateState)
        }

        assertThat(exception).hasMessageThat().contains("todo")
    }

    @Test
    fun `should handle adding first state to project with empty states list`() = runTest{
        // Given
        val newState = State(id = "s1", name = "TODO")
        val existingProject = Project(id = UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), projectName = "Test Project", states = emptyList())

        coEvery { projectRepository.getProjectById(any()) } returns existingProject

        // When
        addStateToProjectUseCase.invoke("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a", newState)

        // Then
        coVerify {
            projectRepository.editProject(
                match {
                    it.id == UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a") && it.states.size == 1 && it.states[0] == newState
                }
            )
        }
    }
}