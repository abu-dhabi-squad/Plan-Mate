package logic.repository

import logic.model.Task

interface TaskRepository {
   suspend fun getAllTasks(): List<Task>
   suspend fun getTaskById(taskId: String): Task?
   suspend fun getTaskByProjectId(projectId: String): List<Task>
   suspend fun createTask(task: Task)
   suspend fun editTask(updatedTask: Task)
   suspend fun deleteTask(taskId: String)
}