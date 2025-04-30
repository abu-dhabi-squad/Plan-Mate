package squad.abudhabi.logic.task

import squad.abudhabi.logic.model.Task
import squad.abudhabi.logic.repository.TaskRepository
import squad.abudhabi.logic.validation.TaskValidator
import java.time.LocalDate

class CreateTaskUseCase(
    private val taskRepository: TaskRepository,
    private val taskValidator: TaskValidator,
) {

    operator fun invoke(
        userId: String,
        projectId: String,
        stateId: String,
        title: String,
        description: String,
        startDate: String,
        endDate: String
    ): Boolean {
        taskValidator.validateOrThrow(startDate, endDate)
        taskRepository.createTask(Task(
            userName = userId,
            projectId = projectId,
            stateId = stateId,
            title = title,
            description = description,
            startDate = LocalDate.parse(startDate),
            endDate = LocalDate.parse(endDate),
        ))
        return true
    }
}