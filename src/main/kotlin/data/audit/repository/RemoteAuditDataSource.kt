package data.audit.repository

import data.audit.model.AuditDto

interface RemoteAuditDataSource {
    suspend fun createAuditLog(audit: AuditDto)
    suspend fun getAuditByEntityId(entityId: String): List<AuditDto>
}