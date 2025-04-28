package squad.abudhabi.logic.task

import squad.abudhabi.logic.exceptions.NoTasksFoundException
import squad.abudhabi.logic.model.Task
import squad.abudhabi.logic.repository.TaskRepository

class GetTasksByProjectIdUseCase(private val taskRepository: TaskRepository) {

    operator fun invoke(projectId: String): List<Task> {
        val tasksResult = taskRepository.getAllTasks().filter { it.projectId == projectId }
        if (tasksResult.isEmpty()) throw NoTasksFoundException()
        return tasksResult
    }
}