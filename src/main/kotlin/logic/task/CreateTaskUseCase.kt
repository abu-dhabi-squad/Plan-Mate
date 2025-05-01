package squad.abudhabi.logic.task

import squad.abudhabi.logic.model.Task
import squad.abudhabi.logic.repository.TaskRepository
import squad.abudhabi.logic.validation.TaskValidator

class CreateTaskUseCase(private val taskRepository: TaskRepository, private val taskValidator: TaskValidator, ) {
    operator fun invoke(task: Task) {
        taskValidator.validateOrThrow(task)
        taskRepository.createTask(task)
    }
}