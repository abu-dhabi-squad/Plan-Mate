package data.audit.datasource.csvdatasource

import logic.model.Audit

interface LocalAuditDataSource {

    suspend fun createAuditLog(audit: Audit)
    suspend fun getAuditByEntityId(entityId: String) : List<Audit>
}