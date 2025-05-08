package logic.project

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import logic.exceptions.ProjectNotFoundException
import logic.model.Project
import logic.model.State
import logic.repository.ProjectRepository

class EditProjectUIUseCaseTest {
    private lateinit var editProjectUseCase: EditProjectUseCase
    private val projectRepository: ProjectRepository = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        editProjectUseCase = EditProjectUseCase(projectRepository)
    }

    @Test
    fun `editProject should throw ProjectNotFoundException when the projectRepository getProjectById returns null`() {
        //given
        val state = State("id1", "stateName")
        val project = Project("id1", "name1", listOf(state))
        val newName = "name2"
        every { projectRepository.getProjectById(any()) } returns null
        //when & then
        assertThrows<ProjectNotFoundException> {
            editProjectUseCase(project.id,newName)
        }
    }

    @Test
    fun `editProject should throw Exception when the projectRepository getProjectById throw Exception`() {
        //given
        val state = State("id1", "stateName")
        val project = Project("id1", "name1", listOf(state))
        val newName = "name2"
        every { projectRepository.getProjectById(any()) } throws Exception()
        //when & then
        assertThrows<Exception> {
            editProjectUseCase(project.id,newName)
        }
    }

    @Test
    fun `editProject should throw Exception when the projectRepository editProject throw Exception`() {
        //given
        val state = State("id1", "stateName")
        val project = Project("id1", "name1", listOf(state))
        val newName = "name2"
        every { projectRepository.editProject(any()) } throws Exception()
        every { projectRepository.getProjectById(any()) } returns project
        //when & then
        assertThrows<Exception> {
            editProjectUseCase(project.id,newName)
        }
    }

    @Test
    fun `editProject should call projectRepository editProject function when the id is found`() {
        //given
        val state = State("id1", "stateName")
        val project = Project("id1", "name1", listOf(state))
        val newName = "name2"
        every { projectRepository.getProjectById(any()) } returns project
        //when
        editProjectUseCase(project.id,newName)
        //then
        verify (exactly = 1){ projectRepository.editProject(any()) }
    }
}