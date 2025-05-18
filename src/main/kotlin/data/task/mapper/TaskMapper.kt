package data.task.mapper

import data.task.model.TaskDto
import logic.model.Task
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class TaskMapper {
    private val dateFormatter = DateTimeFormatter.ISO_DATE
    fun taskToDto(task: Task): TaskDto {
        return TaskDto(
            _id = task.taskId.toString(),
            userName = task.username,
            projectId = task.projectId.toString(),
            stateId = task.taskStateId.toString(),
            title = task.title,
            description = task.description,
            startDate = task.startDate.format(dateFormatter),
            endDate = task.endDate.format(dateFormatter)
        )
    }

    fun dtoToTask(taskDto: TaskDto): Task {
        return Task(
            taskId = Uuid.parse(taskDto._id),
            username = taskDto.userName,
            projectId = Uuid.parse(taskDto.projectId),
            taskStateId = Uuid.parse(taskDto.stateId),
            title = taskDto.title,
            description = taskDto.description,
            startDate = LocalDate.parse(taskDto.startDate, dateFormatter),
            endDate = LocalDate.parse(taskDto.endDate, dateFormatter)
        )
    }
}