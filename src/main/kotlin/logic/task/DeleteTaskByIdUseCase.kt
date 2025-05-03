package logic.task

import squad.abudhabi.logic.exceptions.TaskNotFoundException
import squad.abudhabi.logic.repository.TaskRepository

class DeleteTaskByIdUseCase(private val taskRepository: TaskRepository) {
    operator fun invoke(taskId: String) {
        taskRepository.getTaskById(taskId) ?: throw TaskNotFoundException()
        taskRepository.deleteTask(taskId)
    }
}