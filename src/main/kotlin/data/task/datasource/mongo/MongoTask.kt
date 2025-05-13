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
        return taskCollection.find(Filters.eq(TASK_ID, taskId)).firstOrNull()
    }

    override suspend fun getTaskByProjectId(projectId: String): List<TaskDto> {
        return taskCollection.find(Filters.eq(PROJECT_ID, projectId)).toList()
    }

    override suspend fun createTask(task: TaskDto) {
        taskCollection.insertOne(task)
    }

    override suspend fun editTask(task: TaskDto) {
        val updateDoc = Document("\$set", Document().apply {
            append(USER_NAME, task.userName)
            append(PROJECT_ID, task.projectId)
            append(TASK_STATE_ID, task.stateId)
            append(TASK_TITLE, task.title)
            append(TASK_DESCRIPTION, task.description)
            append(TASK_START_DATE, task.startDate)
            append(TASK_END_DATE, task.endDate)
        })
        taskCollection.updateOne(
            Filters.eq(TASK_ID, task.id),
            updateDoc
        )
    }

    override suspend fun deleteTask(taskId: String) {
        taskCollection.deleteOne(Filters.eq(TASK_ID, taskId))
    }

    override suspend fun deleteTasksByProjectById(projectId: String) {
        taskCollection.deleteMany(Filters.eq(PROJECT_ID, projectId))
    }

    private companion object{
        const val PROJECT_ID = "projectId"
        const val TASK_ID = "id"
        const val USER_NAME = "userName"
        const val TASK_STATE_ID = "stateId"
        const val TASK_TITLE = "title"
        const val TASK_DESCRIPTION = "description"
        const val TASK_START_DATE = "startDate"
        const val TASK_END_DATE = "endDate"
    }
}
