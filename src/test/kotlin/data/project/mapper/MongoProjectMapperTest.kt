package data.project.mapper

import com.google.common.truth.Truth.assertThat
import data.project.model.ProjectDto
import data.project.model.StateDto
import logic.model.Project
import logic.model.State
import java.util.*
import kotlin.test.Test

class MongoProjectMapperTest {

    private val mapper = MongoProjectMapper()

    @Test
    fun `dtoToProject should map ProjectDto to Project correctly`() {
        val projectDto = ProjectDto(
            id = "123e4567-e89b-12d3-a456-426614174000",
            projectName = "My Project",
            states = listOf(
                StateDto("1", "State A"),
                StateDto("2", "State B")
            )
        )

        val result = mapper.dtoToProject(projectDto)

        assertThat(result.id).isEqualTo(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
        assertThat(result.projectName).isEqualTo("My Project")
        assertThat(result.states).hasSize(2)
        assertThat(result.states[0].id).isEqualTo("1")
        assertThat(result.states[0].name).isEqualTo("State A")
    }

    @Test
    fun `projectToDto should map Project to ProjectDto correctly`() {
        val project = Project(
            id = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
            projectName = "My Project",
            states = listOf(
                State("1", "State A"),
                State("2", "State B")
            )
        )

        val result = mapper.projectToDto(project)

        assertThat(result.id).isEqualTo("123e4567-e89b-12d3-a456-426614174000")
        assertThat(result.projectName).isEqualTo("My Project")
        assertThat(result.states).hasSize(2)
        assertThat(result.states[1].id).isEqualTo("2")
        assertThat(result.states[1].name).isEqualTo("State B")
    }

    @Test
    fun `dtoToState should map StateDto to State correctly`() {
        val stateDto = StateDto("5", "Ready")
        val result = mapper.dtoToState(stateDto)
        assertThat(result.id).isEqualTo("5")
        assertThat(result.name).isEqualTo("Ready")
    }

    @Test
    fun `stateToDto should map State to StateDto correctly`() {
        val state = State("6", "In Progress")
        val result = mapper.stateToDto(state)
        assertThat(result.id).isEqualTo("6")
        assertThat(result.name).isEqualTo("In Progress")
    }
}