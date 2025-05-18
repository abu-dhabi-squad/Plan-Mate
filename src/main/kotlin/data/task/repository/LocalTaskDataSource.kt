package data.task.repository

import logic.model.Task
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface LocalTaskDataSource {
    fun getAllTasks(): List<Task>
    fun getTaskById(taskId: Uuid): Task?
    fun getTaskByProjectId(projectId: Uuid): List<Task>
    fun createTask(task: Task)
    fun editTask(task: Task)
    fun deleteTask(taskId: String)
}