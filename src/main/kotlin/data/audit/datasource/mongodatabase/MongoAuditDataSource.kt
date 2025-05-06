package data.audit.datasource.mongodatabase

import com.mongodb.client.MongoCollection
import data.audit.AduitMapper.AuditMapper
import data.audit.AduitMapper.AuditMapperFields.ENTITY_ID_FIELD
import data.audit.datasource.AuditDataSource
import org.bson.Document
import squad.abudhabi.logic.model.Audit

class MongoAuditDataSource(
    private val collection: MongoCollection<Document>,
    private val mapper: AuditMapper
) : AuditDataSource {

    override suspend fun createAuditLog(audit: Audit) {
            val doc = mapper.auditToDocument(audit)
            collection.insertOne(doc)
    }

    override suspend fun getAuditByEntityId(entityId: String): List<Audit> {
        val docs = collection.find(Document(ENTITY_ID_FIELD, entityId)) ?: return emptyList()
        return docs.map { mapper.documentToAudit(it) }.toList()
    }
}
