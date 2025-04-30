package logic.project

import com.google.common.truth.Truth.assertThat
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import squad.abudhabi.logic.project.CreateProjectUseCase
import squad.abudhabi.logic.repository.ProjectRepository

class CreateProjectUseCaseTest{
    private lateinit var projectRepository: ProjectRepository
    private lateinit var createProjectUseCase: CreateProjectUseCase

    @BeforeEach
    fun setup() {
        projectRepository = mockk()
        createProjectUseCase = CreateProjectUseCase(projectRepository)
    }

    @Test
    fun `given valid project name, should create and save project`() {
        // Given
        val name = "Test Project"
        every { projectRepository.addProject(any())} returns true

        // When
        val result = createProjectUseCase.invoke(name)

        // Then
        assertThat(result).isEqualTo(true)
        verify {
            projectRepository.addProject(
                match { project ->
                    project.projectName == name && project.states.isEmpty()
                }
            )
        }
    }

}