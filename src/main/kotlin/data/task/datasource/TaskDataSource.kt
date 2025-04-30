package squad.abudhabi.data.task.datasource

import squad.abudhabi.logic.model.Task

interface TaskDataSource {
    fun getAllTasks(): List<Task>

    fun getTaskById(taskId: String): Task

    fun createTask(task: Task): Boolean

    fun editTask(task: Task): Boolean

    fun deleteTask(taskId: String): Boolean
}