package data.task.repository

import squad.abudhabi.data.task.datasource.TaskDataSource
import squad.abudhabi.logic.model.Task
import squad.abudhabi.logic.repository.TaskRepository

class TaskRepositoryImpl(
    private val taskDataSource: TaskDataSource,
): TaskRepository {
    override fun getAllTasks(): List<Task> {
        return taskDataSource.getAllTasks()
    }

    override fun getTaskById(taskId: String): Task? {
        return taskDataSource.getTaskById(taskId)
    }

    override fun getTaskByProjectId(projectId: String): List<Task> {
        return taskDataSource.getTaskByProjectId(projectId)
    }

    override fun createTask(task: Task) {
        return taskDataSource.createTask(task)
    }

    override fun editTask(updatedTask: Task) {
        return taskDataSource.editTask(updatedTask)
    }

    override fun deleteTask(taskId: String) {
        return taskDataSource.deleteTask(taskId)
    }


}