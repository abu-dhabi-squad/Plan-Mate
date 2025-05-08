package logic.task

import logic.exceptions.TaskNotFoundException
import logic.model.Task
import logic.repository.TaskRepository

class GetTaskByIdUseCase(private val taskRepository: TaskRepository) {
    operator fun invoke(taskId: String): Task =
        taskRepository.getTaskById(taskId) ?: throw TaskNotFoundException()
}