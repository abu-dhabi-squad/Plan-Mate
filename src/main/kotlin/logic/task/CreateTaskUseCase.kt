package logic.task

import squad.abudhabi.logic.model.Task
import squad.abudhabi.logic.repository.TaskRepository
import logic.validation.TaskValidator

class CreateTaskUseCase(private val taskRepository: TaskRepository, private val taskValidator: TaskValidator) {
    suspend operator fun invoke(task: Task) {
        taskValidator.validateOrThrow(task)
        taskRepository.createTask(task)
    }
}