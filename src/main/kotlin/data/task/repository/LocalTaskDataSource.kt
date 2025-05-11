package data.task.repository

import logic.model.Task
import java.util.*

interface LocalTaskDataSource {
    fun getAllTasks(): List<Task>
    fun getTaskById(taskId: UUID): Task?
    fun getTaskByProjectId(projectId: UUID): List<Task>
    fun createTask(task: Task)
    fun editTask(task: Task)
    fun deleteTask(taskId: String)
}