package data.project.mapper

import com.google.common.truth.Truth.assertThat
import data.project.model.ProjectDto
import data.project.model.StateDto
import logic.model.Project
import logic.model.TaskState
import org.junit.jupiter.api.Test
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class ProjectMapperTest {

    private val mapper = ProjectMapper()

    @Test
    fun `dtoToProject should map ProjectDto to Project correctly`() {
        val projectId = Uuid.random()
        val state1Id = Uuid.random()
        val state2Id = Uuid.random()

        val projectDto = ProjectDto(
            _id = projectId.toString(),
            projectName = "Test Project",
            states = listOf(
                StateDto(_id = state1Id.toString(), name = "TaskState 1"),
                StateDto(_id = state2Id.toString(), name = "TaskState 2")
            )
        )

        val result = mapper.dtoToProject(projectDto)

        assertThat(result.projectId).isEqualTo(projectId)
        assertThat(result.projectName).isEqualTo("Test Project")
        assertThat(result.taskStates).hasSize(2)
        assertThat(result.taskStates[0].stateId).isEqualTo(state1Id)
        assertThat(result.taskStates[0].stateName).isEqualTo("TaskState 1")
        assertThat(result.taskStates[1].stateId).isEqualTo(state2Id)
        assertThat(result.taskStates[1].stateName).isEqualTo("TaskState 2")
    }

    @Test
    fun `projectToDto should map Project to ProjectDto correctly`() {
        val projectId = Uuid.random()
        val state1Id = Uuid.random()

        val project = Project(
            projectId = projectId,
            projectName = "Sample Project",
            taskStates = listOf(TaskState(stateId = state1Id, stateName = "Done"))
        )

        val result = mapper.projectToDto(project)

        assertThat(result._id).isEqualTo(projectId.toString())
        assertThat(result.projectName).isEqualTo("Sample Project")
        assertThat(result.states).hasSize(1)
        assertThat(result.states[0]._id).isEqualTo(state1Id.toString())
        assertThat(result.states[0].name).isEqualTo("Done")
    }

    @Test
    fun `dtoToState should map StateDto to State correctly`() {
        val uuid = Uuid.random()
        val stateDto = StateDto(_id = uuid.toString(), name = "Pending")

        val result = mapper.dtoToState(stateDto)

        assertThat(result.stateId).isEqualTo(uuid)
        assertThat(result.stateName).isEqualTo("Pending")
    }

    @Test
    fun `stateToDto should map State to StateDto correctly`() {
        val uuid = Uuid.random()
        val taskState = TaskState(stateId = uuid, stateName = "In Progress")

        val result = mapper.stateToDto(taskState)

        assertThat(result._id).isEqualTo(uuid.toString())
        assertThat(result.name).isEqualTo("In Progress")
    }
}
