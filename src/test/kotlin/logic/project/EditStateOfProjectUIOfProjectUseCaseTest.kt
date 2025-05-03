package logic.project

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import squad.abudhabi.logic.exceptions.ProjectNotFoundException
import squad.abudhabi.logic.exceptions.ProjectStateNotFoundException
import squad.abudhabi.logic.model.Project
import squad.abudhabi.logic.model.State
import squad.abudhabi.logic.project.EditStateOfProjectUseCase
import squad.abudhabi.logic.repository.ProjectRepository

class EditStateOfProjectUIOfProjectUseCaseTest {
    private lateinit var editStateToProjectUseCase: EditStateOfProjectUseCase
    private val projectRepository: ProjectRepository = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        editStateToProjectUseCase = EditStateOfProjectUseCase(projectRepository)
    }

    @Test
    fun `editStateOfProject should throw ProjectNotFoundException when projectRepository getProjectById returns null`() {

        //given
        val newState = State("id1", "newState1")
        val states = listOf(State("id2", "state1"), State("id3", "state2"))
        val project = Project("id1", "name1", states)
        every { projectRepository.getProjectById(any()) } returns null
        //when & then
        assertThrows<ProjectNotFoundException> {
            editStateToProjectUseCase(project.id, newState)
        }
    }

    @Test
    fun `editStateOfProject should throw Exception when projectRepository getProjectById throw Exception`() {
        //given
        val newState = State("id1", "newState1")
        val states = listOf(State("id2", "state1"), State("id3", "state2"))
        val project = Project("id1", "name1", states)
        every { projectRepository.getProjectById(any()) } throws Exception()
        //when & then
        assertThrows<Exception> {
            editStateToProjectUseCase(project.id, newState)
        }
    }

    @Test
    fun `editStateOfProject should throw ProjectStateNotFoundException when the new state id not in project's states`() {
        //given
        val newState = State("id1", "newState1")
        val states = listOf(State("id2", "state1"), State("id3", "state2"))
        val project = Project("id1", "name1", states)
        every { projectRepository.getProjectById(any()) } returns project
        //when & then
        assertThrows<ProjectStateNotFoundException> {
            editStateToProjectUseCase(project.id, newState)
        }
    }

    @Test
    fun `editStateOfProject should throw Exception when the projectRepository editProject throw Exception`() {
        //given
        val newState = State("id1", "newState1")
        val states = listOf(State("id1", "state1"), State("id2", "state2"))
        val project = Project("id1", "name1", states)
        every { projectRepository.getProjectById(any()) } returns project
        every { projectRepository.editProject(any()) } throws Exception()
        //when & then
        assertThrows<Exception> {
            editStateToProjectUseCase(project.id, newState)
        }
    }

    @Test
    fun `editStateOfProject should call projectRepository editProject function when the state id is found`() {
        //given
        val newState = State("id1", "newState1")
        val states = listOf(State("id1", "state1"), State("id2", "state2"))
        val project = Project("id1", "name1", states)
        every { projectRepository.getProjectById(any()) } returns project
        //when
        editStateToProjectUseCase(project.id, newState)
        //then
        verify(exactly = 1) { projectRepository.editProject(any()) }
    }
}