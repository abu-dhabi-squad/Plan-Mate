package logic.task

import logic.exceptions.TaskNotFoundException
import logic.model.Task
import logic.repository.TaskRepository
import logic.validation.TaskValidator

class EditTaskUseCase(private val taskRepository: TaskRepository, private val taskValidator: TaskValidator) {
    suspend operator fun invoke(task: Task) {
        taskValidator.validateOrThrow(task)
        taskRepository.getTaskById(task.id) ?: throw TaskNotFoundException()
        taskRepository.editTask(task)
    }
}