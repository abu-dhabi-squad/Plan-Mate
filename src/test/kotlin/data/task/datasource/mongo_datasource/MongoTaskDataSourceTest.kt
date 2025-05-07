package data.task.datasource.mongo_datasource

import com.google.common.truth.Truth.assertThat
import com.mongodb.client.FindIterable
import com.mongodb.client.MongoCollection
import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.InsertOneResult
import com.mongodb.client.result.UpdateResult
import io.mockk.*
import org.bson.Document
import data.task.mapper.TaskMapper
import kotlinx.coroutines.test.runTest
import logic.model.Task
import org.junit.jupiter.api.BeforeEach
import java.time.LocalDate
import java.util.*
import kotlin.test.Test

class MongoTaskDataSourceTest {

    private lateinit var dataSource: MongoTaskDataSource
    private val collection: MongoCollection<Document> = mockk()
    private val mapper: TaskMapper = mockk()

    @BeforeEach
    fun setup() {
        dataSource = MongoTaskDataSource(collection, mapper)
    }

//    @Test
//    fun `should return all tasks when collection has documents`() = runTest {
//        // Given
//        val doc1 = Document("id", "1")
//        val doc2 = Document("id", "2")
//        val task1 = Task(UUID.randomUUID(), "user1", "proj1", "state1", "title1", "desc1", LocalDate.now(), LocalDate.now())
//        val task2 = Task(UUID.randomUUID(), "user2", "proj2", "state2", "title2", "desc2", LocalDate.now(), LocalDate.now())
//        val documents = listOf(doc1, doc2)
//        val findIterable: FindIterable<Document> = mockk()
//        val iterator: Iterator<Document> = documents.iterator()
//
//        every { collection.find() } returns findIterable
//        every { findIterable.iterator() } returns iterator
//        every { mapper.documentToTask(doc1) } returns task1
//        every { mapper.documentToTask(doc2) } returns task2
//
//        // When
//        val result = dataSource.getAllTasks()
//
//        // Then
//        assertThat(result).isEqualTo(listOf(task1, task2))
//    }

    @Test
    fun `should return task when taskId exists`() = runTest {
        // Given
        val taskId = "12345"
        val doc = Document("id", taskId)
        val task = Task(UUID.randomUUID(), "user", "proj", "state", "title", "desc", LocalDate.now(), LocalDate.now())

        every { collection.find(Document(TaskMapper.TASK_ID_FIELD, taskId)).first() } returns doc
        every { mapper.documentToTask(doc) } returns task

        // When
        val result = dataSource.getTaskById(taskId)

        // Then
        assertThat(result).isEqualTo(task)
    }

    @Test
    fun `should return null when taskId does not exist`() = runTest {
        // Given
        val taskId = "12345"

        every { collection.find(Document(TaskMapper.TASK_ID_FIELD, taskId)).first() } returns null

        // When
        val result = dataSource.getTaskById(taskId)

        // Then
        assertThat(result).isNull()
    }

//    @Test
//    fun `should return tasks for a projectId`() = runTest {
//        // Given
//        val projectId = "proj1"
//        val doc1 = Document("projectId", projectId)
//        val doc2 = Document("projectId", projectId)
//        val task1 = Task(UUID.randomUUID(), "user1", projectId, "state1", "title1", "desc1", LocalDate.now(), LocalDate.now())
//        val task2 = Task(UUID.randomUUID(), "user2", projectId, "state2", "title2", "desc2", LocalDate.now(), LocalDate.now())
//        val documents = listOf(doc1, doc2)
//        val findIterable: FindIterable<Document> = mockk()
//        val iterator: Iterator<Document> = documents.iterator()
//
//        every { collection.find(Document(TaskMapper.TASK_ID_FIELD, projectId)) } returns findIterable
//        every { findIterable.iterator() } returns iterator
//        every { mapper.documentToTask(doc1) } returns task1
//        every { mapper.documentToTask(doc2) } returns task2
//
//        // When
//        val result = dataSource.getTaskByProjectId(projectId)
//
//        // Then
//        assertThat(result).isEqualTo(listOf(task1, task2))
//    }

    @Test
    fun `should create task successfully`() = runTest {
        // Given
        val task = Task(UUID.randomUUID(), "user", "proj", "state", "title", "desc", LocalDate.now(), LocalDate.now())
        val doc = Document("id", task.id.toString())
        val insertOneResult: InsertOneResult = mockk()

        every { mapper.taskToDocument(task) } returns doc
        every { collection.insertOne(doc) } returns insertOneResult

        // When
        dataSource.createTask(task)

        // Then
        verify(exactly = 1) { collection.insertOne(doc) }
    }

    @Test
    fun `should edit task successfully`() = runTest {
        // Given
        val task = Task(UUID.randomUUID(), "user", "proj", "state", "title", "desc", LocalDate.now(), LocalDate.now())
        val updateDoc = Document(
            "\$set", Document(TaskMapper.USERNAME_FIELD, task.userName)
                .append(TaskMapper.PROJECT_ID_FIELD, task.projectId)
                .append(TaskMapper.STATE_ID_FIELD, task.stateId)
                .append(TaskMapper.TITLE_FIELD, task.title)
                .append(TaskMapper.DESCRIPTION_FIELD, task.description)
                .append(TaskMapper.START_DATE, task.startDate.toString())
                .append(TaskMapper.END_DATE, task.endDate.toString())
        )
        val updateResult: UpdateResult = mockk()

        every { collection.updateOne(Document(TaskMapper.TASK_ID_FIELD, task.id), updateDoc) } returns updateResult

        // When
        dataSource.editTask(task)

        // Then
        verify(exactly = 1) { collection.updateOne(Document(TaskMapper.TASK_ID_FIELD, task.id), updateDoc) }
    }

    @Test
    fun `should delete task successfully`() = runTest {
        // Given
        val taskId = "12345"
        val deleteResult: DeleteResult = mockk()

        every { collection.deleteOne(Document(TaskMapper.TASK_ID_FIELD, taskId)) } returns deleteResult

        // When
        dataSource.deleteTask(taskId)

        // Then
        verify(exactly = 1) { collection.deleteOne(Document(TaskMapper.TASK_ID_FIELD, taskId)) }
    }
}