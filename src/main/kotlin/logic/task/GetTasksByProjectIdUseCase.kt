package squad.abudhabi.logic.task

import squad.abudhabi.logic.exceptions.NoTasksFoundException
import squad.abudhabi.logic.model.Task
import squad.abudhabi.logic.repository.TaskRepository

class GetTasksByProjectIdUseCase(private val taskRepository: TaskRepository) {

    operator fun invoke(projectId: String): List<Task> {
        return taskRepository.getAllTasks()
            .filter { it.projectId == projectId }
            .takeIf { it.isNotEmpty() } ?: throw NoTasksFoundException()
    }
}