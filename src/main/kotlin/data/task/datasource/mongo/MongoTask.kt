package data.task.datasource.mongo

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.task.model.TaskDto
import data.task.repository.RemoteTaskDataSource
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.bson.Document

class MongoTask(
    private val taskCollection: MongoCollection<TaskDto>
) : RemoteTaskDataSource {

    override suspend fun getAllTasks(): List<TaskDto> {
        return taskCollection.find().toList()
    }

    override suspend fun getTaskById(taskId: String): TaskDto? {
        return taskCollection.find(Filters.eq(TaskDto::_id.name, taskId)).firstOrNull()
    }

    override suspend fun getTaskByProjectId(projectId: String): List<TaskDto> {
        return taskCollection.find(Filters.eq(TaskDto::projectId.name, projectId)).toList()
    }

    override suspend fun createTask(task: TaskDto) {
        taskCollection.insertOne(task)
    }

    override suspend fun editTask(task: TaskDto) {
        val updateDoc = Document("\$set", Document().apply {
            append(TaskDto::userName.name, task.userName)
            append(TaskDto::projectId.name, task.projectId)
            append(TaskDto::stateId.name, task.stateId)
            append(TaskDto::title.name, task.title)
            append(TaskDto::description.name, task.description)
            append(TaskDto::startDate.name, task.startDate)
            append(TaskDto::endDate.name, task.endDate)
        })
        taskCollection.updateOne(
            Filters.eq(TaskDto::_id.name, task._id),
            updateDoc
        )
    }

    override suspend fun deleteTask(taskId: String) {
        taskCollection.deleteOne(Filters.eq(TaskDto::_id.name, taskId))
    }

    override suspend fun deleteTasksByProjectById(projectId: String) {
        taskCollection.deleteMany(Filters.eq(TaskDto::projectId.name, projectId))
    }

}
