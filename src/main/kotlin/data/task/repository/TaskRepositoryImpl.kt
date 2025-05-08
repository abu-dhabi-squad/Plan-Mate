package data.task.repository

import data.task.mapper.TaskMapper
import logic.model.Task
import logic.repository.TaskRepository

class TaskRepositoryImpl(
    private val remoteTaskDataSource: RemoteTaskDataSource,
    private val remoteTaskParser: TaskMapper
) : TaskRepository {

    override suspend fun getAllTasks(): List<Task> {
        return remoteTaskDataSource.getAllTasks().map { taskDto -> remoteTaskParser.taskDtoToTask(taskDto) }
    }

    override suspend fun getTaskById(taskId: String): Task? {
        return remoteTaskDataSource.getTaskById(taskId)?.let { taskDto ->
            remoteTaskParser.taskDtoToTask(taskDto)
        }
    }

    override suspend fun getTaskByProjectId(projectId: String): List<Task> {
        return remoteTaskDataSource.getTaskByProjectId(projectId).map { taskDto ->
            remoteTaskParser.taskDtoToTask(taskDto)
        }
    }

    override suspend fun createTask(task: Task) {
        val taskDto = remoteTaskParser.taskToTaskDto(task)
        remoteTaskDataSource.createTask(taskDto)
    }

    override suspend fun editTask(updatedTask: Task) {
        val taskDto = remoteTaskParser.taskToTaskDto(updatedTask)
        remoteTaskDataSource.editTask(taskDto)
    }

    override suspend fun deleteTask(taskId: String) {
        remoteTaskDataSource.deleteTask(taskId)
    }
}
