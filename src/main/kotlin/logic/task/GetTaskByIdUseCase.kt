package logic.task

import logic.exceptions.TaskNotFoundException
import logic.model.Task
import logic.repository.TaskRepository
import java.util.UUID

class GetTaskByIdUseCase(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(taskId: UUID): Task =
        taskRepository.getTaskById(taskId) ?: throw TaskNotFoundException()
}