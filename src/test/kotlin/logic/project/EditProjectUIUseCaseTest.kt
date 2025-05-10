package logic.project

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.exceptions.ProjectNotFoundException
import logic.model.Project
import logic.model.State
import logic.repository.ProjectRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID

class EditProjectUIUseCaseTest {
    private lateinit var editProjectUseCase: EditProjectUseCase
    private val projectRepository: ProjectRepository = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        editProjectUseCase = EditProjectUseCase(projectRepository)
    }

    @Test
    fun `editProject should throw ProjectNotFoundException when the projectRepository getProjectById returns null`() = runTest{
        //given
        val state = State(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "stateName")
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", listOf(state))
        val newName = "name2"
        coEvery { projectRepository.getProjectById(any()) } returns null
        //when & then
        assertThrows<ProjectNotFoundException> {
            editProjectUseCase(project.id.toString(),newName)
        }
    }

    @Test
    fun `editProject should throw Exception when the projectRepository getProjectById throw Exception`() = runTest{
        //given
        val state = State(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "stateName")
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", listOf(state))
        val newName = "name2"
        coEvery { projectRepository.getProjectById(any()) } throws Exception()
        //when & then
        assertThrows<Exception> {
            editProjectUseCase(project.id.toString(),newName)
        }
    }

    @Test
    fun `editProject should throw Exception when the projectRepository editProject throw Exception`() = runTest{
        //given
        val state = State(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "stateName")
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", listOf(state))
        val newName = "name2"
        coEvery { projectRepository.editProject(any()) } throws Exception()
        coEvery { projectRepository.getProjectById(any()) } returns project
        //when & then
        assertThrows<Exception> {
            editProjectUseCase(project.id.toString(),newName)
        }
    }

    @Test
    fun `editProject should call projectRepository editProject function when the id is found`() = runTest{
        //given
        val state = State(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "stateName")
        val project = Project(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), "name1", listOf(state))
        val newName = "name2"
        coEvery { projectRepository.getProjectById(any()) } returns project
        //when
        editProjectUseCase(project.id.toString(),newName)
        //then
        coVerify (exactly = 1){ projectRepository.editProject(any()) }
    }
}