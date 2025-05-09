package logic.project

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.exceptions.ProjectNotFoundException
import logic.model.Project
import logic.repository.ProjectRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.util.UUID
import kotlin.test.Test

class GetProjectByIdUseCaseTest {
    private lateinit var projectRepository: ProjectRepository
    private lateinit var getProjectByIdUseCase: GetProjectByIdUseCase

    @BeforeEach
    fun setUp() {
        projectRepository = mockk()
        getProjectByIdUseCase = GetProjectByIdUseCase(projectRepository)
    }

    @Test
    fun `should return the project when a project with the given id exists`() = runTest{
        //Given
        val projectId = "1"
        val project = Project(id = UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), projectName = "Project One", states = emptyList())
        coEvery { projectRepository.getProjectById(projectId) } returns project
        // When
        val result = getProjectByIdUseCase(projectId)
        //Then
        assertThat(result).isEqualTo(project)
        coVerify(exactly = 1) { projectRepository.getProjectById(projectId) }
    }

    @Test
    fun `should throw ProjectNotFoundException when a project with the given id does not exist`()= runTest {
        // Given
        val projectId = "2"
        coEvery { projectRepository.getProjectById(projectId) } returns null
        // When & Then
        assertThrows<ProjectNotFoundException> {
            getProjectByIdUseCase.invoke(projectId)
        }
        coVerify(exactly = 1) { projectRepository.getProjectById(projectId) }
    }

}