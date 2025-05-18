package logic.task

import logic.exceptions.TaskNotFoundException
import logic.model.Task
import logic.repository.TaskRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class GetTaskByIdUseCase(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(taskId: Uuid): Task =
        taskRepository.getTaskById(taskId) ?: throw TaskNotFoundException()
}