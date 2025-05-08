package data.task.datasource

import logic.model.Task

interface TaskDataSource {
    fun getAllTasks(): List<Task>

    fun getTaskById(taskId: String): Task?

    fun getTaskByProjectId(projectId: String): List<Task>

    fun createTask(task: Task)

    fun editTask(task: Task)

    fun deleteTask(taskId: String)
}