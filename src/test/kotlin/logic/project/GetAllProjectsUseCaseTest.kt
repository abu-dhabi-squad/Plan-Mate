import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import squad.abudhabi.logic.exceptions.NoProjectsFoundException
import squad.abudhabi.logic.model.Project
import logic.project.GetAllProjectsUseCase
import squad.abudhabi.logic.repository.ProjectRepository
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
    fun `should return the project when a project with the given id exists`() {
        //Given
        val projects = listOf(
            Project(id = "1", projectName = "Project One", states = emptyList()),
            Project(id = "2", projectName = "Project Two", states = emptyList())
        )
        every { projectRepository.getAllProjects() } returns projects
        // When
        val result = getAllProjectsUseCase()
        //Then
        assertThat(result).isEqualTo(projects)
        verify(exactly = 1) { projectRepository.getAllProjects() }
    }

    @Test
    fun `should throw NoProjectsFoundException when no projects exist`() {
        // Given
        every { projectRepository.getAllProjects() } returns emptyList()
        // When & Then
        assertThrows<NoProjectsFoundException> {
            getAllProjectsUseCase()
        }
        verify(exactly = 1) { projectRepository.getAllProjects() }
    }


}