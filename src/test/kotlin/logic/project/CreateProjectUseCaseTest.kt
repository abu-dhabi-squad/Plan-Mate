package logic.project

import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import squad.abudhabi.logic.model.Project
import squad.abudhabi.logic.repository.ProjectRepository

class CreateProjectUseCaseTest{
    private lateinit var projectRepository: ProjectRepository
    private lateinit var createProjectUseCase: CreateProjectUseCase

    @BeforeEach
    fun setup() {
        projectRepository = mockk(relaxed = true)
        createProjectUseCase = CreateProjectUseCase(projectRepository)
    }

    @Test
    fun `given valid project name, should create and save project`() {
        // Given
        val name = "Test Project"

        // When
        createProjectUseCase.invoke(Project(projectName = name, states = listOf()))

        // Then
        verify {
            projectRepository.addProject(
                match { project ->
                    project.projectName == name && project.states.isEmpty()
                }
            )
        }
    }

}