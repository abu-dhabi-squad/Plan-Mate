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
        return taskCollection.find(Filters.eq("id", taskId)).firstOrNull()
    }

    override suspend fun getTaskByProjectId(projectId: String): List<TaskDto> {
        return taskCollection.find(Filters.eq("projectId", projectId)).toList()
    }

    override suspend fun createTask(task: TaskDto) {
        taskCollection.insertOne(task)
    }

    override suspend fun editTask(task: TaskDto) {
        val updateDoc = Document("\$set", Document().apply {
            append("userName", task.userName)
            append("projectId", task.projectId)
            append("stateId", task.stateId)
            append("title", task.title)
            append("description", task.description)
            append("startDate", task.startDate)
            append("endDate", task.endDate)
        })
        taskCollection.updateOne(
            Filters.eq("id", task.id),
            updateDoc
        )
    }

    override suspend fun deleteTask(taskId: String) {
        taskCollection.deleteOne(Filters.eq("id", taskId))
    }

    override suspend fun deleteTasksByProjectById(projectId: String) {
        taskCollection.deleteMany(Filters.eq("projectId", projectId))
    }
}
