package logic.task

import logic.exceptions.TaskNotFoundException
import logic.repository.TaskRepository
import java.util.*

class DeleteTaskByIdUseCase(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(taskId: UUID) {
        taskRepository.getTaskById(taskId) ?: throw TaskNotFoundException()
        taskRepository.deleteTask(taskId)
    }
}