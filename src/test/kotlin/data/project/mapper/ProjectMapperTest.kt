package data.project.mapper

import com.google.common.truth.Truth.assertThat
import data.project.model.ProjectDto
import data.project.model.StateDto
import logic.model.Project
import logic.model.State
import org.junit.jupiter.api.Test
import java.util.*

class ProjectMapperTest {

    private val mapper = ProjectMapper()

    @Test
    fun `projectDtoToProject should map correctly`() {
        val dto = ProjectDto(
            id = UUID.randomUUID().toString(),
            projectName = "Test Project",
            states = listOf(StateDto("1", "To Do"), StateDto("2", "Done"))
        )

        val result = mapper.projectDtoToProject(dto)

        assertThat(result.id.toString()).isEqualTo(dto.id)
        assertThat(result.projectName).isEqualTo(dto.projectName)
        assertThat(result.states.size).isEqualTo(2)
        assertThat(result.states[0].id).isEqualTo("1")
        assertThat(result.states[0].name).isEqualTo("To Do")
    }

    @Test
    fun `projectToProjectDto should map correctly`() {
        val project = Project(
            id = UUID.randomUUID(),
            projectName = "Test Project",
            states = listOf(State("1", "To Do"), State("2", "Done"))
        )

        val result = mapper.projectToProjectDto(project)

        assertThat(result.id).isEqualTo(project.id.toString())
        assertThat(result.projectName).isEqualTo(project.projectName)
        assertThat(result.states.size).isEqualTo(2)
        assertThat(result.states[1].name).isEqualTo("Done")
    }

    @Test
    fun `stateDtoToState should map correctly`() {
        val stateDto = StateDto("5", "In Progress")
        val result = mapper.stateDtoToState(stateDto)
        assertThat(result.id).isEqualTo("5")
        assertThat(result.name).isEqualTo("In Progress")
    }

    @Test
    fun `stateToStateDto should map correctly`() {
        val state = State("8", "Blocked")
        val result = mapper.stateToStateDto(state)
        assertThat(result.id).isEqualTo("8")
        assertThat(result.name).isEqualTo("Blocked")
    }
}
