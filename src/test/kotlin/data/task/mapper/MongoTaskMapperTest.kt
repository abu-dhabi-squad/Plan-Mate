package data.task.mapper

import com.google.common.truth.Truth.assertThat
import data.task.model.TaskDto
import helper.createTask

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID
import kotlin.test.Test

class MongoTaskMapperTest {

    private val mapper = MongoTaskMapper()
    private val dateFormatter = DateTimeFormatter.ISO_DATE

    @Test
    fun `taskToTaskDto maps correctly`() {
        // Given
        val task = createTask()

        // When
        val dto = mapper.taskToTaskDto(task)

        // Then
        assertThat(dto.id).isEqualTo(task.id.toString())
        assertThat(dto.userName).isEqualTo(task.userName)
        assertThat(dto.projectId).isEqualTo(task.projectId)
        assertThat(dto.stateId).isEqualTo(task.stateId)
        assertThat(dto.title).isEqualTo(task.title)
        assertThat(dto.description).isEqualTo(task.description)
        assertThat(dto.startDate).isEqualTo(task.startDate.format(dateFormatter))
        assertThat(dto.endDate).isEqualTo(task.endDate.format(dateFormatter))
    }

    @Test
    fun `taskDtoToTask maps correctly`() {
        // Given
        val id = UUID.randomUUID()
        val startDate = LocalDate.now()
        val endDate = startDate.plusDays(5)

        val dto = TaskDto(
            id = id.toString(),
            userName = "testUser",
            projectId = "project123",
            stateId = "state123",
            title = "Task title",
            description = "Task description",
            startDate = startDate.format(dateFormatter),
            endDate = endDate.format(dateFormatter)
        )

        // When
        val task = mapper.taskDtoToTask(dto)

        // Then
        assertThat(task.id).isEqualTo(id)
        assertThat(task.userName).isEqualTo(dto.userName)
        assertThat(task.projectId).isEqualTo(dto.projectId)
        assertThat(task.stateId).isEqualTo(dto.stateId)
        assertThat(task.title).isEqualTo(dto.title)
        assertThat(task.description).isEqualTo(dto.description)
        assertThat(task.startDate).isEqualTo(startDate)
        assertThat(task.endDate).isEqualTo(endDate)
    }
}