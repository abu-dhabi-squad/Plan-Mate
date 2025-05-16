package logic.repository

import logic.model.Task
import java.util.*

interface TaskRepository {
    suspend fun getAllTasks(): List<Task>
    suspend fun getTaskById(taskId: UUID): Task?
    suspend fun getTaskByProjectId(projectId: UUID): List<Task>
    suspend fun createTask(task: Task)
    suspend fun editTask(updatedTask: Task)
    suspend fun deleteTask(taskId: UUID)
    suspend fun deleteTasksByProjectById(projectId: UUID)
}