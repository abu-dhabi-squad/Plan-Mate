package data.task.repository

import data.task.mapper.TaskMapper
import logic.model.Task
import logic.repository.TaskRepository
import java.util.*

class TaskRepositoryImpl(
    private val remoteTaskDataSource: RemoteTaskDataSource,
    private val taskMapper: TaskMapper
) : TaskRepository {

    override suspend fun getAllTasks(): List<Task> {
        return remoteTaskDataSource.getAllTasks().map { taskDto -> taskMapper.dtoToTask(taskDto) }
    }

    override suspend fun getTaskById(taskId: UUID): Task? {
        return remoteTaskDataSource.getTaskById(taskId.toString())?.let { taskDto ->
            taskMapper.dtoToTask(taskDto)
        }
    }

    override suspend fun getTaskByProjectId(projectId: UUID): List<Task> {
        return remoteTaskDataSource.getTaskByProjectId(projectId.toString()).map { taskDto ->
            taskMapper.dtoToTask(taskDto)
        }
    }

    override suspend fun createTask(task: Task) {
        val taskDto = taskMapper.taskToDto(task)
        remoteTaskDataSource.createTask(taskDto)
    }

    override suspend fun editTask(updatedTask: Task) {
        val taskDto = taskMapper.taskToDto(updatedTask)
        remoteTaskDataSource.editTask(taskDto)
    }

    override suspend fun deleteTask(taskId: UUID) {
        remoteTaskDataSource.deleteTask(taskId.toString())
    }

    override suspend fun deleteTasksByProjectById(projectId: UUID) {
        remoteTaskDataSource.deleteTasksByProjectById(projectId.toString())
    }
}
