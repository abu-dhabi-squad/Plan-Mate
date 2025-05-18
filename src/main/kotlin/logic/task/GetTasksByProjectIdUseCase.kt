package logic.task

import logic.exceptions.NoTasksFoundException
import logic.model.Task
import logic.repository.TaskRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class GetTasksByProjectIdUseCase(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(projectId: Uuid): List<Task> =
        taskRepository.getTaskByProjectId(projectId).takeIf { it.isNotEmpty() } ?: throw NoTasksFoundException()
}