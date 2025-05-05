package logic.task

import squad.abudhabi.logic.exceptions.NoTasksFoundException
import squad.abudhabi.logic.model.Task
import squad.abudhabi.logic.repository.TaskRepository

class GetTasksByProjectIdUseCase(private val taskRepository: TaskRepository) {

    suspend operator fun invoke(projectId: String): List<Task> =
        taskRepository.getTaskByProjectId(projectId).takeIf { it.isNotEmpty() } ?: throw NoTasksFoundException()
}