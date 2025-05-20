package logic.task.validation

import logic.exceptions.InvalidTaskDateException
import logic.model.Task

class TaskValidatorImpl : TaskValidator {
    override fun validateOrThrow(task: Task) {
        if (task.endDate < task.startDate) {
            throw InvalidTaskDateException()
        }
    }
}