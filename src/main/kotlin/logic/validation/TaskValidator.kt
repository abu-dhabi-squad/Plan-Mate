package squad.abudhabi.logic.validation

import squad.abudhabi.logic.model.Task

interface TaskValidator {
    fun validateOrThrow(task: Task)
}