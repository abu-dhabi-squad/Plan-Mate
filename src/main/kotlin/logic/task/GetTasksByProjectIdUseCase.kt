package logic.task

import logic.exceptions.NoTasksFoundException
import logic.model.Task
import logic.repository.TaskRepository

class GetTasksByProjectIdUseCase(private val taskRepository: TaskRepository) {

    operator fun invoke(projectId: String): List<Task> =
        taskRepository.getTaskByProjectId(projectId).takeIf { it.isNotEmpty() } ?: throw NoTasksFoundException()
}