package squad.abudhabi.logic.task

import squad.abudhabi.logic.exceptions.TaskNotFoundException
import squad.abudhabi.logic.model.Task
import squad.abudhabi.logic.repository.TaskRepository
import squad.abudhabi.logic.validation.TaskValidator

class EditTaskUseCase(private val taskRepository: TaskRepository, private val taskValidator: TaskValidator) {
    operator fun invoke(task: Task) {
        taskValidator.validateOrThrow(task)
        taskRepository.getTaskById(task.id) ?: throw TaskNotFoundException()
        taskRepository.editTask(task)
    }
}