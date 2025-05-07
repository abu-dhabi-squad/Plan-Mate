import com.google.common.truth.Truth.assertThat
import io.mockk.*
import kotlinx.coroutines.test.runTest
import logic.exceptions.NoProjectsFoundException
import logic.model.Project
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import logic.project.GetAllProjectsUseCase
import logic.repository.ProjectRepository
import java.util.*
import kotlin.test.Test

class GetAllProjectsUseCaseTest {
    private lateinit var projectRepository: ProjectRepository
    private lateinit var getAllProjectsUseCase: GetAllProjectsUseCase

    @BeforeEach
    fun setUp() {
        projectRepository = mockk()
        getAllProjectsUseCase = GetAllProjectsUseCase(projectRepository)
    }

    @Test
    fun `should return the project when a project with the given id exists`()= runTest {
        //Given
        val projects = listOf(
            Project(id = UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), projectName = "Project One", states = emptyList()),
            Project(id =UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"), projectName = "Project Two", states = emptyList())
        )
        coEvery { projectRepository.getAllProjects() } returns projects
        // When
        val result = getAllProjectsUseCase()
        //Then
        assertThat(result).isEqualTo(projects)
        coVerify(exactly = 1) { projectRepository.getAllProjects() }
    }

    @Test
    fun `should throw NoProjectsFoundException when no projects exist`() = runTest{
        // Given
        coEvery { projectRepository.getAllProjects() } returns emptyList()
        // When & Then
        assertThrows<NoProjectsFoundException> {
            getAllProjectsUseCase()
        }
        coVerify(exactly = 1) { projectRepository.getAllProjects() }
    }


}