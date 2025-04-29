package logic.project

import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import squad.abudhabi.logic.exceptions.CanNotEditException
import squad.abudhabi.logic.exceptions.DataNotFoundException
import squad.abudhabi.logic.model.Project
import squad.abudhabi.logic.model.State
import squad.abudhabi.logic.project.EditProjectUseCase
import squad.abudhabi.logic.repository.ProjectRepository

class EditProjectUseCaseTest {

    private lateinit var editProjectUseCase: EditProjectUseCase
    private val projectRepository: ProjectRepository = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        editProjectUseCase = EditProjectUseCase(projectRepository)
    }


    @Test
    fun `editProject should throw CanNotEditException when the new project data has empty states`() {
        //given
        val state = State("id1", "stateName")
        val newProject = Project("id1", "name1", listOf())
        //when & then
        assertThrows<CanNotEditException> {
            editProjectUseCase.editProject(newProject)
        }
    }

    @Test
    fun `editProject should throw DataNotFoundException when the projectRepository getProjects returns empty list`() {
        //given
        val state = State("id1", "stateName")
        val newProject = Project("id1", "name2", listOf(state))
        every { projectRepository.getProjects() } returns listOf()
        //when & then
        assertThrows<DataNotFoundException> {
            editProjectUseCase.editProject(newProject)
        }
    }

    @Test
    fun `editProject should throw Exception when the projectRepository getProjects throw Exception`() {
        //given
        val state = State("id1", "stateName")
        val newProject = Project("id1", "name2", listOf(state))
        every { projectRepository.getProjects() } throws Exception()
        //when & then
        assertThrows<Exception> {
            editProjectUseCase.editProject(newProject)
        }
    }

    @Test
    fun `editProject should throw CanNotEditException when the new project id not in the ids of all projects`() {
        //given
        val state = State("id1", "stateName")
        val project = Project("id1", "name1", listOf(state))
        val newProject = Project("id2", "name2", listOf(state))
        every { projectRepository.getProjects() } returns listOf(project)
        //when & then
        assertThrows<CanNotEditException> {
            editProjectUseCase.editProject(newProject)
        }
    }

    @Test
    fun `editProject should throw Exception when the projectRepository editProject throw Exception`() {
        //given
        val state = State("id1", "stateName")
        val newProject = Project("id1", "name2", listOf(state))
        every { projectRepository.editProject(any()) } throws Exception()
        //when & then
        assertThrows<Exception> {
            editProjectUseCase.editProject(newProject)
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `editProject should return like projectRepository editProject function when it return true or false`(
        returnedValue: Boolean
    ) {
        //given
        val state = State("id1", "stateName")
        val project = Project("id1", "name1", listOf(state))
        val newProject = Project("id1", "name2", listOf(state))
        every { projectRepository.getProjects() } returns listOf(project)
        every { projectRepository.editProject(any()) } returns returnedValue
        //when & then
        Truth.assertThat(editProjectUseCase.editProject(newProject)).isEqualTo(returnedValue)
    }

}