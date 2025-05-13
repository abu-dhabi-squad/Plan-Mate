package data.task.mapper

import com.google.common.truth.Truth.assertThat
import data.task.model.TaskDto
import helper.createTask
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID
import kotlin.test.Test

class TaskMapperTest {

    private val mapper = TaskMapper()
    private val dateFormatter = DateTimeFormatter.ISO_DATE

    @Test
    fun `taskToDto maps correctly`() {
        // Given
        val task = createTask()

        // When
        val dto = mapper.taskToDto(task)

        // Then
        assertThat(dto.id).isEqualTo(task.taskId.toString())
        assertThat(dto.userName).isEqualTo(task.username)
        assertThat(dto.projectId).isEqualTo(task.projectId.toString())
        assertThat(dto.stateId).isEqualTo(task.taskStateId.toString())
        assertThat(dto.title).isEqualTo(task.title)
        assertThat(dto.description).isEqualTo(task.description)
        assertThat(dto.startDate).isEqualTo(task.startDate.format(dateFormatter))
        assertThat(dto.endDate).isEqualTo(task.endDate.format(dateFormatter))
    }

    @Test
    fun `dtoToTask maps correctly`() {
        // Given
        val id = UUID.randomUUID()
        val stateId = UUID.randomUUID()
        val projectId = UUID.randomUUID()
        val startDate = LocalDate.now()
        val endDate = startDate.plusDays(5)

        val dto = TaskDto(
            id = id.toString(),
            userName = "testUser",
            projectId = projectId.toString(),
            stateId = stateId.toString(),
            title = "Task title",
            description = "Task description",
            startDate = startDate.format(dateFormatter),
            endDate = endDate.format(dateFormatter)
        )

        // When
        val task = mapper.dtoToTask(dto)

        // Then
        assertThat(task.taskId.toString()).isEqualTo(dto.id)
        assertThat(task.username).isEqualTo(dto.userName)
        assertThat(task.projectId.toString()).isEqualTo(dto.projectId)
        assertThat(task.taskStateId.toString()).isEqualTo(dto.stateId)
        assertThat(task.title).isEqualTo(dto.title)
        assertThat(task.description).isEqualTo(dto.description)
        assertThat(task.startDate).isEqualTo(startDate)
        assertThat(task.endDate).isEqualTo(endDate)
    }
}