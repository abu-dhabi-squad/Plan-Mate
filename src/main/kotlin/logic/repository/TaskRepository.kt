package squad.abudhabi.logic.repository

import squad.abudhabi.logic.model.Task

interface TaskRepository {
    fun getAllTasks(): List<Task>
    fun getTaskById(taskId: String): Task
    fun createTask(task: Task): Boolean
    fun editTask(updatedTask: Task): Boolean
    fun deleteTask(taskId: String): Boolean
}