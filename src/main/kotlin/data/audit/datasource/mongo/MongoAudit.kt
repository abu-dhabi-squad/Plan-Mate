package data.audit.datasource.mongo

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.audit.model.AuditDto
import data.audit.repository.RemoteAuditDataSource
import kotlinx.coroutines.flow.toList

class MongoAudit(
    private val auditCollection: MongoCollection<AuditDto>
) : RemoteAuditDataSource {

    override suspend fun createAuditLog(audit: AuditDto) {
        auditCollection.insertOne(audit)
    }

    override suspend fun getAuditByEntityId(entityId: String): List<AuditDto> {
        val filter = Filters.eq(AuditDto::entityId.name, entityId)
        return auditCollection.find(filter).toList()
    }

}