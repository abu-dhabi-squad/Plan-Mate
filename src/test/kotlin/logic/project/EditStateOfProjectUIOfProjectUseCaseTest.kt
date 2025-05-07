package logic.project

import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import squad.abudhabi.logic.exceptions.ProjectNotFoundException
import squad.abudhabi.logic.exceptions.ProjectStateNotFoundException
import squad.abudhabi.logic.model.Project
import squad.abudhabi.logic.model.State
import squad.abudhabi.logic.repository.ProjectRepository
import java.util.*

class EditStateOfProjectUIOfProjectUseCaseTest {
    private lateinit var editStateToProjectUseCase: EditStateOfProjectUseCase
    private val projectRepository: ProjectRepository = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        editStateToProjectUseCase = EditStateOfProjectUseCase(projectRepository)
    }

    @Test
    fun `editStateOfProject should throw ProjectNotFoundException when projectRepository getProjectById returns null`() = runTest{

        //given
        val newState = State("id1", "newState1")
        val states = listOf(State("id2", "state1"), State("id3", "state2"))
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", states)
        coEvery { projectRepository.getProjectById(any()) } returns null
        //when & then
        assertThrows<ProjectNotFoundException> {
            editStateToProjectUseCase(project.id.toString(), newState)
        }
    }

    @Test
    fun `editStateOfProject should throw Exception when projectRepository getProjectById throw Exception`() = runTest{
        //given
        val newState = State("id1", "newState1")
        val states = listOf(State("id2", "state1"), State("id3", "state2"))
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", states)
        coEvery { projectRepository.getProjectById(any()) } throws Exception()
        //when & then
        assertThrows<Exception> {
            editStateToProjectUseCase(project.id.toString(), newState)
        }
    }

    @Test
    fun `editStateOfProject should throw ProjectStateNotFoundException when the new state id not in project's states`() = runTest{
        //given
        val newState = State("id1", "newState1")
        val states = listOf(State("id2", "state1"), State("id3", "state2"))
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", states)
        coEvery { projectRepository.getProjectById(any()) } returns project
        //when & then
        assertThrows<ProjectStateNotFoundException> {
            editStateToProjectUseCase(project.id.toString(), newState)
        }
    }

    @Test
    fun `editStateOfProject should throw Exception when the projectRepository editProject throw Exception`() = runTest{
        //given
        val newState = State("id1", "newState1")
        val states = listOf(State("id1", "state1"), State("id2", "state2"))
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", states)
        coEvery { projectRepository.getProjectById(any()) } returns project
        coEvery { projectRepository.editProject(any()) } throws Exception()
        //when & then
        assertThrows<Exception> {
            editStateToProjectUseCase(project.id.toString(), newState)
        }
    }

    @Test
    fun `editStateOfProject should call projectRepository editProject function when the state id is found`()= runTest {
        //given
        val newState = State("id1", "newState1")
        val states = listOf(State("id1", "state1"), State("id2", "state2"))
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", states)
        coEvery { projectRepository.getProjectById(any()) } returns project
        //when
        editStateToProjectUseCase(project.id.toString(), newState)
        //then
        coVerify(exactly = 1) { projectRepository.editProject(any()) }
    }
}