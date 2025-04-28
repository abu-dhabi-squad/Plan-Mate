package squad.abudhabi.logic.repository

import squad.abudhabi.logic.model.Task

interface TaskRepository {
    fun createTask(task: Task)
    fun editTask(taskId: String, updatedTask: Task)
    fun deleteTaskById(taskId: String)
    fun getAllTasks(): List<Task>
}