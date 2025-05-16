package data.task.repository

import data.task.model.TaskDto

interface RemoteTaskDataSource {
    suspend fun getAllTasks(): List<TaskDto>
    suspend fun getTaskById(taskId: String): TaskDto?
    suspend fun getTaskByProjectId(projectId: String): List<TaskDto>
    suspend fun createTask(task: TaskDto)
    suspend fun editTask(task: TaskDto)
    suspend fun deleteTask(taskId: String)
    suspend fun deleteTasksByProjectById(projectId: String)
}

