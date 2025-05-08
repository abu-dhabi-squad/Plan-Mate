package data.task.datasource.mongo_datasource

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.task.model.TaskDto
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.bson.Document

class MongoTaskDataSource(
    private val collection: MongoCollection<TaskDto>
) : RemoteTaskDataSource {

    override suspend fun getAllTasks(): List<TaskDto> {
        return collection.find().toList()
    }

    override suspend fun getTaskById(taskId: String): TaskDto? {
        return collection.find(Filters.eq("id", taskId)).firstOrNull()
    }

    override suspend fun getTaskByProjectId(projectId: String): List<TaskDto> {
        return collection.find(Filters.eq("projectId", projectId)).toList()
    }

    override suspend fun createTask(task: TaskDto) {
        collection.insertOne(task)
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
        collection.updateOne(
            Filters.eq("id", task.id),
            updateDoc
        )
    }

    override suspend fun deleteTask(taskId: String) {
        collection.deleteOne(Filters.eq("id", taskId))
    }
}
