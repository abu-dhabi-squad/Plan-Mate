package logic.task

import logic.exceptions.TaskNotFoundException
import logic.repository.TaskRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class DeleteTaskByIdUseCase(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(taskId: Uuid) {
        taskRepository.getTaskById(taskId) ?: throw TaskNotFoundException()
        taskRepository.deleteTask(taskId)
    }
}