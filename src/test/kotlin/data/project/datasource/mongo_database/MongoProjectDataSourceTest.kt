package data.project.datasource.mongo_database

import com.mongodb.client.FindIterable
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoCursor
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.client.result.UpdateResult
import data.audit.datasource.mongo_database.MongoAuditDataSource
import data.audit.mapper.AuditMapper
import data.project.datasource.mongo_datasource.MongoProjectDataSource
import data.project.mapper.ProjectMapper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.test.runTest
import logic.model.Project
import logic.model.State
import org.bson.Document
import org.bson.conversions.Bson
import org.junit.jupiter.api.BeforeEach
import java.util.*
import kotlin.test.Test

class MongoProjectDataSourceTest {

    private lateinit var collection: MongoCollection<Document>
    private lateinit var mapper: ProjectMapper
    private lateinit var dataSource: MongoProjectDataSource

    @BeforeEach
    fun setup() {
        collection = mockk(relaxed = true)
        mapper = mockk(relaxed = true)
        dataSource = MongoProjectDataSource(collection, mapper)
    }

    @Test
    fun `should return empty list when no projects found`() = runTest {
        // Given
        coEvery { collection.find().map { doc -> mapper.documentToProject(doc) }.toList() } returns emptyList()

        // When
        val result = dataSource.getAllProjects()

        // Then
        assertEquals(emptyList<Project>(), result)
    }

    @Test
    fun `should create project when valid project provided`() = runTest {
        // Given
        val project = Project(projectName = "Test Project", states = emptyList())
        val doc = mockk<Document>()
        coEvery { mapper.projectToDocument(project) } returns doc

        // When
        dataSource.createProject(project)

        // Then
        coVerify(exactly = 1) { collection.insertOne(doc) }
    }

//    @Test
//    fun `should update project when valid project provided`() = runTest {
//        // Given
//        val project = Project(
//            id = UUID.randomUUID(),
//            projectName = "Updated Project",
//            states = listOf(State("1", "State 1"))
//        )
//
//        val statesDocs = listOf(
//            Document()
//                .append(ProjectMapper.STATE_ID_FIELD, "1")
//                .append(ProjectMapper.STATE_NAME_FIELD, "State 1")
//        )
//
//        val filter = Filters.eq(ProjectMapper.ID_FIELD, project.id.toString())
//        val update = Updates.combine(
//            Updates.set(ProjectMapper.PROJECT_NAME_FIELD, "Updated Project"),
//            Updates.set(ProjectMapper.STATES_FIELD, statesDocs)
//        )
//
//        coEvery { collection.updateOne(any(), any<Bson>()) } returns mockk<UpdateResult>()
//
//        // When
//        dataSource.editProject(project)
//
//        // Then
//        coVerify(exactly = 1) {
//            collection.updateOne(filter, update)
//        }
//    }


    //returns Unit
    @Test
    fun `should delete project when valid projectId provided`() = runTest {
        // Given
        val projectId = "12345"
        coEvery { collection.deleteOne(any()) } returns null

        // When
        dataSource.deleteProject(projectId)

        // Then
        coVerify(exactly = 1) { collection.deleteOne(Document("id", projectId)) }
    }

    @Test
    fun `should return null when project not found by id`() = runTest {
        // Given
        val projectId = "12345"
        coEvery { collection.find(Document("id", projectId)).first() } returns null

        // When
        val result = dataSource.getProjectById(projectId)

        // Then
        assertNull(result)
    }

    @Test
    fun `should return project when project found by id`() = runTest {
        // Given
        val doc = mockk<Document>()
        val project = Project(
            id = UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"),
            projectName = "Test Project",
            states = emptyList()
        )
        coEvery { collection.find(Document("id", "d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a")).first() } returns doc
        coEvery { mapper.documentToProject(doc) } returns project

        // When
        val result = dataSource.getProjectById("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a")

        // Then
        assertEquals(project, result)
    }

}
