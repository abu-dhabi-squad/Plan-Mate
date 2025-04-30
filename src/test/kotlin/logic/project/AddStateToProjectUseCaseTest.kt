package logic.project

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import squad.abudhabi.logic.exceptions.DuplicateStateException
import squad.abudhabi.logic.exceptions.InvalidStateException
import squad.abudhabi.logic.exceptions.ProjectNotFoundException
import squad.abudhabi.logic.model.Project
import squad.abudhabi.logic.model.State
import squad.abudhabi.logic.project.AddStateToProjectUseCase
import squad.abudhabi.logic.repository.ProjectRepository

class AddStateToProjectUseCaseTest{
    private lateinit var projectRepository: ProjectRepository
    private lateinit var addStateToProjectUseCase: AddStateToProjectUseCase

    @BeforeEach
    fun setup() {
        projectRepository = mockk()
        addStateToProjectUseCase = AddStateToProjectUseCase(projectRepository)
    }

    @Test
    fun `should add new state to existing project`() {
        // Given
        val existingState = State(id = "s1", name = "TODO")
        val newState = State(id = "s2", name = "InProgress")
        val existingProject = Project(id = "p1", projectName = "Test Project", states = listOf(existingState))

        every { projectRepository.getProjects() } returns listOf(existingProject)
        every { projectRepository.editProject(any()) } returns true

        // When
        addStateToProjectUseCase.execute("p1", newState)

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
    fun `should throw InvalidStateException if state name or id is blank`() {
        // Given
        val invalidState = State(id = "", name = "")

        // When & Then
        val exception = assertThrows<InvalidStateException> {
            addStateToProjectUseCase.execute(projectId = "p1", newState = invalidState)
        }
        assertThat(exception).hasMessageThat().contains("State name and ID cannot be blank")
    }

    @Test
    fun `should throw DuplicateStateException if state name already exists`() {
        // Given
        val existingState = State(id = "s1", name = "TODO")
        val duplicateState = State(id = "s2", name = "TODO")
        val existingProject = Project(id = "p1", projectName = "Test Project", states = listOf(existingState))

        every { projectRepository.getProjects() } returns listOf(existingProject)

        // When & Then
        val exception = assertThrows<DuplicateStateException> {
            addStateToProjectUseCase.execute("p1", duplicateState)
        }
        assertThat(exception).hasMessageThat().contains("TODO")
    }

    @Test
    fun `should throw ProjectNotFoundException if project id is invalid`() {
        // Given
        every { projectRepository.getProjects() } returns emptyList()
        val newState = State(id = "s1", name = "Review")

        // When & Then
        val exception = assertThrows<ProjectNotFoundException> {
            addStateToProjectUseCase.execute("invalid_project", newState)
        }
        assertThat(exception).hasMessageThat().contains("invalid_project")
    }

    @Test
    fun `should throw DuplicateStateException if state name matches existing one ignoring case`() {
        // Given
        val existingState = State(id = "s1", name = "ToDo")
        val duplicateState = State(id = "s2", name = "todo") // same name, different case
        val existingProject = Project(id = "p1", projectName = "Test Project", states = listOf(existingState))

        every { projectRepository.getProjects() } returns listOf(existingProject)

        // When & Then
        val exception = assertThrows<DuplicateStateException> {
            addStateToProjectUseCase.execute("p1", duplicateState)
        }

        assertThat(exception).hasMessageThat().contains("todo")
    }

    @Test
    fun `should allow adding state with same ID but different name`() {
        // Given
        val existingState = State(id = "s1", name = "TODO")
        val newState = State(id = "s1", name = "NEW_NAME")
        val existingProject = Project(id = "p1", projectName = "Test Project", states = listOf(existingState))

        every { projectRepository.getProjects() } returns listOf(existingProject)
        every { projectRepository.editProject(any()) } returns true

        // When
        addStateToProjectUseCase.execute("p1", newState)

        // Then
        verify {
            projectRepository.editProject(
                match {
                    it.id == "p1" && it.states.any { s -> s.id == "s1" && s.name == "NEW_NAME" }
                }
            )
        }
    }

    @Test
    fun `should handle adding first state to project with empty states list`() {
        // Given
        val newState = State(id = "s1", name = "TODO")
        val existingProject = Project(id = "p1", projectName = "Test Project", states = emptyList())

        every { projectRepository.getProjects() } returns listOf(existingProject)
        every { projectRepository.editProject(any()) } returns true

        // When
        addStateToProjectUseCase.execute("p1", newState)

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