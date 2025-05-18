package logic.repository

import logic.model.Task
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
@OptIn(ExperimentalUuidApi::class)
interface TaskRepository {
    suspend fun getAllTasks(): List<Task>
    suspend fun getTaskById(taskId: Uuid): Task?
    suspend fun getTaskByProjectId(projectId: Uuid): List<Task>
    suspend fun createTask(task: Task)
    suspend fun editTask(updatedTask: Task)
    suspend fun deleteTask(taskId: Uuid)
    suspend fun deleteTasksByProjectById(projectId: Uuid)
}