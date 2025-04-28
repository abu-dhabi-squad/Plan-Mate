package squad.abudhabi.logic.task

import squad.abudhabi.logic.exceptions.TaskNotFoundException
import squad.abudhabi.logic.repository.TaskRepository
import squad.abudhabi.logic.validation.TaskValidator
import java.time.LocalDate

class EditTaskUseCase(private val taskRepository: TaskRepository, private val taskValidator: TaskValidator) {
    operator fun invoke(
        taskId: String,
        stateId: String,
        title: String,
        description: String,
        startDate: String,
        endDate: String,
    ): Boolean {
        taskValidator.validateOrThrow(startDate, endDate)
        val oldTask = getTaskById(taskId) ?: throw TaskNotFoundException()
        val newTask = oldTask.copy(
            stateId = stateId.ifBlank { oldTask.stateId },
            title = title.ifBlank { oldTask.title },
            description = description.ifBlank { oldTask.description },
            startDate = if (startDate.isNotBlank()) LocalDate.parse(startDate) else oldTask.startDate,
            endDate = if (endDate.isNotBlank()) LocalDate.parse(endDate) else oldTask.endDate,
        )
        taskRepository.editTask(taskId, newTask)
        return true
    }

    private fun getTaskById(taskId: String) = taskRepository.getAllTasks().find { it.id == taskId }
}