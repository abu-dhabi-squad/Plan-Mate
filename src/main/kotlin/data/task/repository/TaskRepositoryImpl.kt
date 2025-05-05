package data.task.repository

import squad.abudhabi.data.task.datasource.TaskDataSource
import squad.abudhabi.logic.model.Task
import squad.abudhabi.logic.repository.TaskRepository

class TaskRepositoryImpl(
    private val taskDataSource: TaskDataSource,
) : TaskRepository {
    override suspend fun getAllTasks(): List<Task> {
        return taskDataSource.getAllTasks()
    }

    override suspend fun getTaskById(taskId: String): Task? {
        return taskDataSource.getTaskById(taskId)
    }

    override suspend fun getTaskByProjectId(projectId: String): List<Task> {
        return taskDataSource.getTaskByProjectId(projectId)
    }

    override suspend fun createTask(task: Task) {
        return taskDataSource.createTask(task)
    }

    override suspend fun editTask(updatedTask: Task) {
        return taskDataSource.editTask(updatedTask)
    }

    override suspend fun deleteTask(taskId: String) {
        return taskDataSource.deleteTask(taskId)
    }


}