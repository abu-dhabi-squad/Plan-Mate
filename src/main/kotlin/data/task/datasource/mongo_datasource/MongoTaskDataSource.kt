package data.task.datasource.mongo_datasource

import com.mongodb.client.MongoCollection
import data.task.mapper.TaskMapper
import org.bson.Document
import squad.abudhabi.data.task.datasource.TaskDataSource
import squad.abudhabi.logic.model.Task

class MongoTaskDataSource(private val collection: MongoCollection<Document>, private val mapper: TaskMapper) :
    TaskDataSource {
    override suspend fun getAllTasks(): List<Task> {
        return collection.find().map { doc ->
            mapper.documentToTask(doc)
        }.toList()
    }

    override suspend fun getTaskById(taskId: String): Task? {
        val doc = collection.find(Document(TaskMapper.Companion.TASK_ID_FIELD, taskId)).first() ?: return null
        return mapper.documentToTask(doc)
    }

    override suspend fun getTaskByProjectId(projectId: String): List<Task> {
        val doc = collection.find(Document(TaskMapper.Companion.TASK_ID_FIELD, projectId)) ?: return emptyList()
        return doc.map { mapper.documentToTask(it) }.toList()
    }

    override suspend fun createTask(task: Task) {
        val doc = mapper.taskToDocument(task)
        collection.insertOne(doc)
    }

    override suspend fun editTask(task: Task) {
        val updateDoc = Document(
            "\$set", Document(TaskMapper.Companion.USERNAME_FIELD, task.userName)
                .append(TaskMapper.Companion.PROJECT_ID_FIELD, task.projectId)
                .append(TaskMapper.Companion.STATE_ID_FIELD, task.stateId)
                .append(TaskMapper.Companion.TITLE_FIELD, task.title)
                .append(TaskMapper.Companion.DESCRIPTION_FIELD, task.description)
                .append(TaskMapper.Companion.START_DATE, task.startDate.toString())
                .append(
                    TaskMapper.Companion.END_DATE, task.endDate.toString(
                    )
                )
        )
        collection.updateOne(Document(TaskMapper.Companion.TASK_ID_FIELD, task.id), updateDoc)
    }

    override suspend fun deleteTask(taskId: String) {
        collection.deleteOne(Document(TaskMapper.Companion.TASK_ID_FIELD, taskId))
    }
}