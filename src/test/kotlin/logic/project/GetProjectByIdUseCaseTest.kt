package logic.project

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import squad.abudhabi.logic.exceptions.ProjectNotFoundException
import squad.abudhabi.logic.model.Project
import squad.abudhabi.logic.repository.ProjectRepository
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
    fun `should return the project when a project with the given id exists`() {
        //Given
        val projectId = "1"
        val project = Project(id = "1", projectName = "Project One", states = emptyList())
        every { projectRepository.getProjectById(projectId) } returns project
        // When
        val result = getProjectByIdUseCase(projectId)
        //Then
        assertThat(result).isEqualTo(project)
        verify(exactly = 1) { projectRepository.getProjectById(projectId) }
    }

    @Test
    fun `should throw ProjectNotFoundException when a project with the given id does not exist`() {
        // Given
        val projectId = "2"
        every { projectRepository.getProjectById(projectId) } returns null
        // When & Then
        assertThrows<ProjectNotFoundException> {
            getProjectByIdUseCase.invoke(projectId)
        }
        verify(exactly = 1) { projectRepository.getProjectById(projectId) }
    }

}