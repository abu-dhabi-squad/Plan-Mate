package data.audit.datasource.mongo_database

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.audit.mapper.AuditMapperFields.ENTITY_ID_FIELD
import data.audit.model.AuditDto
import kotlinx.coroutines.flow.toList

class MongoAuditDataSource(
    private val collection: MongoCollection<AuditDto>
) : RemoteAuditDataSource {

    override suspend fun createAuditLog(audit: AuditDto) {
            collection.insertOne(audit)
    }
    override suspend fun getAuditByEntityId(entityId: String): List<AuditDto> {
        val filter = Filters.eq(ENTITY_ID_FIELD,entityId )
          return collection.find(filter).toList()
    }
}
