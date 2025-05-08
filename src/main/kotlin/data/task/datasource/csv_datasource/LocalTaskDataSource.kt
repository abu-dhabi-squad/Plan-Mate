package data.task.datasource.csv_datasource

import logic.model.Task

interface LocalTaskDataSource {
    suspend fun getAllTasks(): List<Task>
    suspend fun getTaskById(taskId: String): Task?
    suspend fun getTaskByProjectId(projectId: String): List<Task>
    suspend fun createTask(task: Task)
    suspend fun editTask(task: Task)
    suspend fun deleteTask(taskId: String)
}