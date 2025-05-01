package logic.project

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import squad.abudhabi.logic.exceptions.DuplicateStateException
import squad.abudhabi.logic.exceptions.ProjectNotFoundException
import squad.abudhabi.logic.model.Project
import squad.abudhabi.logic.model.State
import squad.abudhabi.logic.project.AddStateToProjectUseCase
import squad.abudhabi.logic.repository.ProjectRepository

class AddStateToProjectUseCaseTest {
    private lateinit var projectRepository: ProjectRepository
    private lateinit var addStateToProjectUseCase: AddStateToProjectUseCase

    @BeforeEach
    fun setup() {
        projectRepository = mockk(relaxed = true)
        addStateToProjectUseCase = AddStateToProjectUseCase(projectRepository)
    }

    @Test
    fun `should add new state to existing project`() {
        // Given
        val existingState = State(id = "s1", name = "TODO")
        val newState = State(id = "s2", name = "InProgress")
        val existingProject = Project(id = "p1", projectName = "Test Project", states = listOf(existingState))

        every { projectRepository.getProjectById("p1") } returns existingProject

        // When
        addStateToProjectUseCase("p1", newState)

        // Then
        verify {
            projectRepository.editProject(
                match {
                    it.id == "p1" && it.states.contains(newState)
                }
            )
        }
    }

    @Test
    fun `should throw DuplicateStateException if state name already exists`() {
        // Given
        val existingState = State(id = "s1", name = "TODO")
        val duplicateState = State(id = "s2", name = "TODO")
        val existingProject = Project(id = "p1", projectName = "Test Project", states = listOf(existingState))

        every { projectRepository.getProjectById(any()) } returns existingProject

        // When & Then
        val exception = assertThrows<DuplicateStateException> {
            addStateToProjectUseCase.invoke("p1", duplicateState)
        }
        assertThat(exception).hasMessageThat().contains("TODO")
    }

    @Test
    fun `should throw Project Not FoundException if project id is invalid`() {
        // Given
        every { projectRepository.getProjectById(any()) } returns null
        val newState = State(id = "s1", name = "Review")

        // When & Then
        assertThrows<ProjectNotFoundException> {
            addStateToProjectUseCase.invoke("invalid_project", newState)
        }
    }

    @Test
    fun `should throw DuplicateStateException if state name matches existing one ignoring case`() {
        // Given
        val existingState = State(id = "s1", name = "ToDo")
        val duplicateState = State(id = "s2", name = "todo") // same name, different case
        val existingProject = Project(id = "p1", projectName = "Test Project", states = listOf(existingState))

        every { projectRepository.getProjectById(any()) } returns existingProject

        // When & Then
        val exception = assertThrows<DuplicateStateException> {
            addStateToProjectUseCase.invoke("p1", duplicateState)
        }

        assertThat(exception).hasMessageThat().contains("todo")
    }

    @Test
    fun `should handle adding first state to project with empty states list`() {
        // Given
        val newState = State(id = "s1", name = "TODO")
        val existingProject = Project(id = "p1", projectName = "Test Project", states = emptyList())

        every { projectRepository.getProjectById(any()) } returns existingProject

        // When
        addStateToProjectUseCase.invoke("p1", newState)

        // Then
        verify {
            projectRepository.editProject(
                match {
                    it.id == "p1" && it.states.size == 1 && it.states[0] == newState
                }
            )
        }
    }
}