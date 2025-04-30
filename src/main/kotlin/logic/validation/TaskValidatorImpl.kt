package squad.abudhabi.logic.validation

import squad.abudhabi.logic.exceptions.InvalidTaskDateException
import squad.abudhabi.logic.model.Task


class TaskValidatorImpl: TaskValidator {
    override fun validateOrThrow(task: Task) {
        if (task.endDate.isBefore(task.startDate)) throw InvalidTaskDateException()
    }
}
