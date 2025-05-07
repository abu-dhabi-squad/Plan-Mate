package data.task.mapper

import com.google.common.truth.Truth.assertThat
import logic.model.Task
import org.bson.Document
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.*

class TaskMapperTest {
    private lateinit var taskMapper: TaskMapper

    @BeforeEach
    fun setup() {
        taskMapper = TaskMapper()
    }

    private val sampleTaskId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000")
    private val sampleStartDate = LocalDate.of(2025, 5, 1)
    private val sampleEndDate = LocalDate.of(2025, 5, 10)

    private val sampleTask = Task(
        id = sampleTaskId,
        userName = "testuser",
        projectId = "project123",
        stateId = "state456",
        title = "Test Task",
        description = "This is a test task.",
        startDate = sampleStartDate,
        endDate = sampleEndDate
    )

    @Test
    fun `should convert Task to Document correctly`() {
        // when
        val document = taskMapper.taskToDocument(sampleTask)

        // then
        assertThat(document.getString("id")).isEqualTo(sampleTaskId.toString())
        assertThat(document.getString("username")).isEqualTo("testuser")
        assertThat(document.getString("projectId")).isEqualTo("project123")
        assertThat(document.getString("stateId")).isEqualTo("state456")
        assertThat(document.getString("title")).isEqualTo("Test Task")
        assertThat(document.getString("description")).isEqualTo("This is a test task.")
        assertThat(document.getString("startDate")).isEqualTo("2025-05-01")
        assertThat(document.getString("endDate")).isEqualTo("2025-05-10")
    }

    @Test
    fun `should convert Document to Task correctly`() {
        // given
        val document = Document()
            .append("id", sampleTaskId.toString())
            .append("username", "testuser")
            .append("projectId", "project123")
            .append("stateId", "state456")
            .append("title", "Test Task")
            .append("description", "This is a test task.")
            .append("startDate", "2025-05-01")
            .append("endDate", "2025-05-10")

        // when
        val task = taskMapper.documentToTask(document)

        // then
        assertThat(task).isEqualTo(sampleTask)
    }
}
