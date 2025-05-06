package data.audit.datasource

import logic.model.Audit

interface AuditDataSource {

    suspend fun createAuditLog(audit: Audit)
    suspend fun getAuditByEntityId(entityId: String) : List<Audit>
}