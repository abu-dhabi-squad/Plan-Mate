package logic.task

import logic.exceptions.NoTasksFoundException
import logic.model.Task
import logic.repository.TaskRepository
import java.util.UUID

class GetTasksByProjectIdUseCase(private val taskRepository: TaskRepository) {

    suspend operator fun invoke(projectId: UUID): List<Task> =
        taskRepository.getTaskByProjectId(projectId).takeIf { it.isNotEmpty() } ?: throw NoTasksFoundException()
}