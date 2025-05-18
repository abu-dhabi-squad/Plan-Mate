package data.task.mapper

import com.google.common.truth.Truth.assertThat
import data.task.model.TaskDto
import helper.createTask
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import kotlin.uuid.Uuid
import kotlin.test.Test
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class TaskMapperTest {

    private val mapper = TaskMapper()

    @Test
    fun `taskToDto maps correctly`() {
        // Given
        val task = createTask()

        // When
        val dto = mapper.taskToDto(task)

        // Then
        assertThat(dto._id).isEqualTo(task.taskId.toString())
        assertThat(dto.userName).isEqualTo(task.username)
        assertThat(dto.projectId).isEqualTo(task.projectId.toString())
        assertThat(dto.stateId).isEqualTo(task.taskStateId.toString())
        assertThat(dto.title).isEqualTo(task.title)
        assertThat(dto.description).isEqualTo(task.description)
        // LocalDate.toString() outputs ISO-8601 by default
        assertThat(dto.startDate).isEqualTo(task.startDate.toString())
        assertThat(dto.endDate).isEqualTo(task.endDate.toString())
    }

    @Test
    fun `dtoToTask maps correctly`() {
        // Given
        val id = Uuid.random()
        val stateId = Uuid.random()
        val projectId = Uuid.random()
        val startDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val endDate = startDate.plus(5, DateTimeUnit.DAY)

        val dto = TaskDto(
            _id = id.toString(),
            userName = "testUser",
            projectId = projectId.toString(),
            stateId = stateId.toString(),
            title = "Task title",
            description = "Task description",
            startDate = startDate.toString(),
            endDate = endDate.toString()
        )

        // When
        val task = mapper.dtoToTask(dto)

        // Then
        assertThat(task.taskId.toString()).isEqualTo(dto._id)
        assertThat(task.username).isEqualTo(dto.userName)
        assertThat(task.projectId.toString()).isEqualTo(dto.projectId)
        assertThat(task.taskStateId.toString()).isEqualTo(dto.stateId)
        assertThat(task.title).isEqualTo(dto.title)
        assertThat(task.description).isEqualTo(dto.description)
        assertThat(task.startDate).isEqualTo(startDate)
        assertThat(task.endDate).isEqualTo(endDate)
    }
}
