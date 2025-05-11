package presentation.logic.task.validation

import logic.model.Task

interface TaskValidator {
    fun validateOrThrow(task: Task)
}