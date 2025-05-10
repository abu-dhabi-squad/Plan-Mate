package data.task.mapper

import data.task.model.TaskDto
import logic.model.Task
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

class MongoTaskMapper {
    private val dateFormatter = DateTimeFormatter.ISO_DATE
    fun taskToDto(task: Task): TaskDto {
        return TaskDto(
            id = task.id.toString(),
            userName = task.userName,
            projectId = task.projectId.toString(),
            stateId = task.stateId.toString(),
            title = task.title,
            description = task.description,
            startDate = task.startDate.format(dateFormatter),
            endDate = task.endDate.format(dateFormatter)
        )
    }
    fun dtoToTask(taskDto: TaskDto): Task {
        return Task(
            id = UUID.fromString(taskDto.id),
            userName = taskDto.userName,
            projectId = UUID.fromString(taskDto.projectId),
            stateId = UUID.fromString(taskDto.stateId),
            title = taskDto.title,
            description = taskDto.description,
            startDate = LocalDate.parse(taskDto.startDate, dateFormatter),
            endDate = LocalDate.parse(taskDto.endDate, dateFormatter)
        )
    }
}