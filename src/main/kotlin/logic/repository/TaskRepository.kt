package squad.abudhabi.logic.repository

import squad.abudhabi.logic.model.Task

interface TaskRepository {
    fun getAllTasks(): List<Task>
    fun getTaskById(taskId: String): Task?
    fun getTaskByProjectId(projectId: String): List<Task>
    fun createTask(task: Task)
    fun editTask(updatedTask: Task)
    fun deleteTask(taskId: String)
}