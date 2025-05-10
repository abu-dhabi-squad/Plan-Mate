package data.project.mapper

import com.google.common.truth.Truth.assertThat
import data.project.model.ProjectDto
import data.project.model.StateDto
import logic.model.Project
import logic.model.State
import org.junit.jupiter.api.Test
import java.util.*

class MongoProjectMapperTest {

    private val mapper = MongoProjectMapper()

    @Test
    fun `dtoToProject should map ProjectDto to Project correctly`() {
        val projectId = UUID.randomUUID()
        val state1Id = UUID.randomUUID()
        val state2Id = UUID.randomUUID()

        val projectDto = ProjectDto(
            id = projectId.toString(),
            projectName = "Test Project",
            states = listOf(
                StateDto(id = state1Id.toString(), name = "State 1"),
                StateDto(id = state2Id.toString(), name = "State 2")
            )
        )

        val result = mapper.dtoToProject(projectDto)

        assertThat(result.id).isEqualTo(projectId)
        assertThat(result.projectName).isEqualTo("Test Project")
        assertThat(result.states).hasSize(2)
        assertThat(result.states[0].id).isEqualTo(state1Id)
        assertThat(result.states[0].name).isEqualTo("State 1")
        assertThat(result.states[1].id).isEqualTo(state2Id)
        assertThat(result.states[1].name).isEqualTo("State 2")
    }

    @Test
    fun `projectToDto should map Project to ProjectDto correctly`() {
        val projectId = UUID.randomUUID()
        val state1Id = UUID.randomUUID()

        val project = Project(
            id = projectId,
            projectName = "Sample Project",
            states = listOf(State(id = state1Id, name = "Done"))
        )

        val result = mapper.projectToDto(project)

        assertThat(result.id).isEqualTo(projectId.toString())
        assertThat(result.projectName).isEqualTo("Sample Project")
        assertThat(result.states).hasSize(1)
        assertThat(result.states[0].id).isEqualTo(state1Id.toString())
        assertThat(result.states[0].name).isEqualTo("Done")
    }

    @Test
    fun `dtoToState should map StateDto to State correctly`() {
        val uuid = UUID.randomUUID()
        val stateDto = StateDto(id = uuid.toString(), name = "Pending")

        val result = mapper.dtoToState(stateDto)

        assertThat(result.id).isEqualTo(uuid)
        assertThat(result.name).isEqualTo("Pending")
    }

    @Test
    fun `stateToDto should map State to StateDto correctly`() {
        val uuid = UUID.randomUUID()
        val state = State(id = uuid, name = "In Progress")

        val result = mapper.stateToDto(state)

        assertThat(result.id).isEqualTo(uuid.toString())
        assertThat(result.name).isEqualTo("In Progress")
    }
}
