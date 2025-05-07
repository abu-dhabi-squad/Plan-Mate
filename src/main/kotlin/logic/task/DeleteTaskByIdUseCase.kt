package logic.task

import logic.exceptions.TaskNotFoundException
import logic.repository.TaskRepository

class DeleteTaskByIdUseCase(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(taskId: String) {
        taskRepository.getTaskById(taskId) ?: throw TaskNotFoundException()
        taskRepository.deleteTask(taskId)
    }
}