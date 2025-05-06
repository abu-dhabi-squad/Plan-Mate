package logic.task

import squad.abudhabi.logic.exceptions.TaskNotFoundException
import squad.abudhabi.logic.model.Task
import squad.abudhabi.logic.repository.TaskRepository

class GetTaskByIdUseCase(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(taskId: String): Task =
        taskRepository.getTaskById(taskId) ?: throw TaskNotFoundException()
}