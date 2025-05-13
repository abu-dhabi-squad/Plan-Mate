package data.task.repository

import data.task.mapper.TaskMapper
import logic.model.Task
import logic.repository.TaskRepository
import presentation.data.utils.BaseRepository
import java.util.UUID

class TaskRepositoryImpl(
    private val remoteTaskDataSource: RemoteTaskDataSource,
    private val taskMapper: TaskMapper
) : TaskRepository, BaseRepository() {

    override suspend fun getAllTasks(): List<Task> = wrapResponse {
        remoteTaskDataSource.getAllTasks().map { taskDto -> taskMapper.dtoToTask(taskDto) }
    }

    override suspend fun getTaskById(taskId: UUID): Task? {
        return wrapResponse {
            remoteTaskDataSource.getTaskById(taskId.toString())?.let { taskDto ->
                taskMapper.dtoToTask(taskDto)
            }
        }
    }

    override suspend fun getTaskByProjectId(projectId: UUID): List<Task> {
        return wrapResponse {
            remoteTaskDataSource.getTaskByProjectId(projectId.toString()).map { taskDto ->
                taskMapper.dtoToTask(taskDto)
            }
        }
    }

    override suspend fun createTask(task: Task) {
        val taskDto = taskMapper.taskToDto(task)
        wrapResponse { remoteTaskDataSource.createTask(taskDto) }
    }

    override suspend fun editTask(updatedTask: Task) {
        val taskDto = taskMapper.taskToDto(updatedTask)
        wrapResponse { remoteTaskDataSource.editTask(taskDto) }
    }

    override suspend fun deleteTask(taskId: UUID) = wrapResponse {
        remoteTaskDataSource.deleteTask(taskId.toString())
    }


    override suspend fun deleteTasksByProjectById(projectId: UUID) = wrapResponse {
        remoteTaskDataSource.deleteTasksByProjectById(projectId.toString())
    }

}
