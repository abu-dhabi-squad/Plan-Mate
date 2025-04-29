package logic.project

import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import squad.abudhabi.logic.exceptions.CanNotEditException
import squad.abudhabi.logic.model.Project
import squad.abudhabi.logic.model.State
import squad.abudhabi.logic.project.EditStateOfProjectUseCase
import squad.abudhabi.logic.repository.ProjectRepository

class EditStateToProjectUseCaseTest {
    private lateinit var editStateToProjectUseCase: EditStateOfProjectUseCase
    private val projectRepository: ProjectRepository = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        editStateToProjectUseCase = EditStateOfProjectUseCase(projectRepository)
    }

    @Test
    fun `editStateOfProject should throw CanNotEditException when project's states is empty`() {
        //given
        val newState = State("id1", "newState1")
        val project = Project("id1", "name1", listOf())
        //when & then
        assertThrows<CanNotEditException> {
            editStateToProjectUseCase.editStateOfProject(project, newState)
        }
    }

    @Test
    fun `editStateOfProject should throw CanNotEditException when the new state id not in project's states`() {
        //given
        val newState = State("id1", "newState1")
        val states = listOf(State("id2", "state1"), State("id3", "state2"))
        val project = Project("id1", "name1", states)
        //when & then
        assertThrows<CanNotEditException> {
            editStateToProjectUseCase.editStateOfProject(project, newState)
        }
    }

    @Test
    fun `editStateOfProject should throw DataNotFoundException when the projectRepository getProjects returns empty list`() {
        //given
        val newState = State("id1", "newState1")
        val states = listOf(State("id1", "state1"), State("id2", "state2"))
        val project = Project("id1", "name1", states)
        every { projectRepository.getProjects() } returns listOf()
        //when & then
        assertThrows<CanNotEditException> {
            editStateToProjectUseCase.editStateOfProject(project, newState)
        }
    }

    @Test
    fun `editStateOfProject should throw Exception when the projectRepository getProjects throw Exception`() {
        //given
        val newState = State("id1", "newState1")
        val states = listOf(State("id1", "state1"), State("id2", "state2"))
        val project = Project("id1", "name1", states)
        every { projectRepository.getProjects() } throws Exception()
        //when & then
        assertThrows<Exception> {
            editStateToProjectUseCase.editStateOfProject(project, newState)
        }
    }

    @Test
    fun `editStateOfProject should throw CanNotEditException when the project id not in the ids of all projects`() {
        //given
        val newState = State("id1", "newState1")
        val states = listOf(State("id1", "state1"), State("id2", "state2"))
        val project = Project("id1", "name1", states)
        val project1 = Project("id2", "name1", states)
        every { projectRepository.getProjects() } returns listOf(project1)
        //when & then
        assertThrows<CanNotEditException> {
            editStateToProjectUseCase.editStateOfProject(project, newState)
        }
    }

    @Test
    fun `editStateOfProject should throw Exception when the projectRepository editProject throw Exception`() {
        //given
        val newState = State("id1", "newState1")
        val states = listOf(State("id1", "state1"), State("id2", "state2"))
        val project = Project("id1", "name1", states)
        every { projectRepository.editProject(any()) } throws Exception()
        //when & then
        assertThrows<Exception> {
            editStateToProjectUseCase.editStateOfProject(project, newState)
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `editStateOfProject should return like projectRepository editProject function when it return true or false`(
        returnedValue: Boolean
    ) {
        //given
        val newState = State("id1", "newState1")
        val states = listOf(State("id1", "state1"), State("id2", "state2"))
        val project = Project("id1", "name1", states)
        every { projectRepository.getProjects() } returns listOf(project)
        every { projectRepository.editProject(any()) } returns returnedValue
        //when & then
        Truth.assertThat(editStateToProjectUseCase.editStateOfProject(project, newState)).isEqualTo(returnedValue)
    }
}