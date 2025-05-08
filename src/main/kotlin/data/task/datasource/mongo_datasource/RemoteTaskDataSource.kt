package data.task.datasource.mongo_datasource

import data.task.model.TaskDto

interface RemoteTaskDataSource {
    suspend fun getAllTasks(): List<TaskDto>
    suspend fun getTaskById(taskId: String): TaskDto?
    suspend fun getTaskByProjectId(projectId: String): List<TaskDto>
    suspend fun createTask(task: TaskDto)
    suspend fun editTask(task: TaskDto)
    suspend fun deleteTask(taskId: String)
}

