package data.project.mapper

import com.google.common.truth.Truth.assertThat
import logic.model.Project
import logic.model.State
import org.bson.Document
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class ProjectMapperTest {
    private lateinit var projectMapper: ProjectMapper

    @BeforeEach
    fun setup() {
        projectMapper = ProjectMapper()
    }

    private val sampleProjectId = UUID.fromString("111e4567-e89b-12d3-a456-426614174abc")
    private val sampleStates = listOf(
        State(id = "state1", name = "To Do"),
        State(id = "state2", name = "In Progress"),
        State(id = "state3", name = "Done")
    )

    private val sampleProject = Project(
        id = sampleProjectId,
        projectName = "Test Project",
        states = sampleStates
    )

    @Test
    fun `should convert Project to Document correctly`() {
        // when
        val document = projectMapper.projectToDocument(sampleProject)

        // then
        assertThat(document.getString("id")).isEqualTo(sampleProjectId.toString())
        assertThat(document.getString("projectName")).isEqualTo("Test Project")

        val statesDocs = document.getList("states", Document::class.java)
        assertThat(statesDocs).hasSize(3)
        assertThat(statesDocs[0].getString("id")).isEqualTo("state1")
        assertThat(statesDocs[0].getString("name")).isEqualTo("To Do")
        assertThat(statesDocs[2].getString("id")).isEqualTo("state3")
        assertThat(statesDocs[2].getString("name")).isEqualTo("Done")
    }

    @Test
    fun `should convert Document to Project correctly`() {
        // given
        val statesDocs = listOf(
            Document().append("id", "state1").append("name", "To Do"),
            Document().append("id", "state2").append("name", "In Progress"),
            Document().append("id", "state3").append("name", "Done")
        )

        val document = Document()
            .append("id", sampleProjectId.toString())
            .append("projectName", "Test Project")
            .append("states", statesDocs)

        // when
        val project = projectMapper.documentToProject(document)

        // then
        assertThat(project.id).isEqualTo(sampleProjectId)
        assertThat(project.projectName).isEqualTo("Test Project")
        assertThat(project.states).hasSize(3)
        assertThat(project.states[0]).isEqualTo(State("state1", "To Do"))
        assertThat(project.states[1]).isEqualTo(State("state2", "In Progress"))
        assertThat(project.states[2]).isEqualTo(State("state3", "Done"))
    }
}
