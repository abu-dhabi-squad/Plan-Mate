package logic.validation

import logic.model.Task

interface TaskValidator {
    fun validateOrThrow(task: Task)
}