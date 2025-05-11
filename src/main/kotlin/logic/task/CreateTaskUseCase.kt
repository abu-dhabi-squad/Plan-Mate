package logic.task

import logic.model.Task
import logic.repository.TaskRepository
import presentation.logic.task.validation.TaskValidator

class CreateTaskUseCase(private val taskRepository: TaskRepository, private val taskValidator: TaskValidator) {
    suspend operator fun invoke(task: Task) {
        taskValidator.validateOrThrow(task)
        taskRepository.createTask(task)
    }
}