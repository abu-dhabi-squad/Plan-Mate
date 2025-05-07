import com.mongodb.client.MongoCollection
import data.audit.datasource.mongo_database.MongoAuditDataSource
import data.audit.mapper.AuditMapper
import io.mockk.*
import kotlinx.coroutines.test.runTest
import logic.model.Audit
import logic.model.EntityType
import org.bson.Document
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime
import java.util.UUID

class MongoAuditDataSourceTest {

    private lateinit var collection: MongoCollection<Document>
    private lateinit var mapper: AuditMapper
    private lateinit var dataSource: MongoAuditDataSource

    @Before
    fun setup() {
        collection = mockk(relaxed = true)
        mapper = mockk(relaxed = true)
        dataSource = MongoAuditDataSource(collection, mapper)
    }

    @Test
    fun `should insert mapped document when createAuditLog is called`() = runTest {
        // Given
        val audit = Audit(
            id = UUID.randomUUID(),
            createdBy = "admin",
            entityId = "entity-123",
            entityType = EntityType.TASK,
            oldState = "inactive",
            newState = "active",
            date = LocalDateTime.now()
        )
        val document = Document("key", "value")
        coEvery { mapper.auditToDocument(audit) } returns document

        // When
        dataSource.createAuditLog(audit)

        // Then
        coVerify { collection.insertOne(document) }
    }

//    @Test
//    fun `should return mapped audits when getAuditByEntityId is called`() = runTest {
//        // Given
//        val entityId = "entity-123"
//        val document1 = Document("doc", "1")
//        val document2 = Document("doc", "2")
//        val documents = listOf(document1, document2)
//
//        val audit1 = Audit(
//            id = UUID.randomUUID(),
//            createdBy = "admin1",
//            entityId = entityId,
//            entityType = EntityType.PROJECT,
//            oldState = "draft",
//            newState = "submitted",
//            date = LocalDateTime.now()
//        )
//        val audit2 = Audit(
//            id = UUID.randomUUID(),
//            createdBy = "admin2",
//            entityId = entityId,
//            entityType = EntityType.PROJECT,
//            oldState = "submitted",
//            newState = "approved",
//            date = LocalDateTime.now()
//        )
//
//        coEvery { collection.find(Document(ENTITY_ID_FIELD, entityId)) } returns documents
//        coEvery { mapper.documentToAudit(document1) } returns audit1
//        coEvery { mapper.documentToAudit(document2) } returns audit2
//
//        // When
//        val result = dataSource.getAuditByEntityId(entityId)
//
//        // Then
//        assertEquals(listOf(audit1, audit2), result)
//    }
}
