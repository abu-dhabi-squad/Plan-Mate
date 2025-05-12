package logic.project

import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import logic.model.Project
import logic.repository.ProjectRepository

class CreateProjectUseCaseTest{
    private lateinit var projectRepository: ProjectRepository
    private lateinit var createProjectUseCase: CreateProjectUseCase

    @BeforeEach
    fun setup() {
        projectRepository = mockk(relaxed = true)
        createProjectUseCase = CreateProjectUseCase(projectRepository)
    }

    @Test
    fun `createProjectUseCase should create and save project when project name is valid`() = runTest{
        // Given
        val name = "Test Project"

        // When
        createProjectUseCase.invoke(Project(projectName = name, taskStates = listOf()))

        // Then
        coVerify {
            projectRepository.addProject(
                match { project ->
                    project.projectName == name && project.taskStates.isEmpty()
                }
            )
        }
    }

}