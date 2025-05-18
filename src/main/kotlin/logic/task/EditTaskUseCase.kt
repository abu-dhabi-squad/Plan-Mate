package logic.task

import logic.exceptions.TaskNotFoundException
import logic.model.Task
import logic.repository.TaskRepository
import logic.task.validation.TaskValidator
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class EditTaskUseCase(private val taskRepository: TaskRepository, private val taskValidator: TaskValidator) {
    suspend operator fun invoke(task: Task) {
        taskValidator.validateOrThrow(task)
        taskRepository.getTaskById(task.taskId) ?: throw TaskNotFoundException()
        taskRepository.editTask(task)
    }
}