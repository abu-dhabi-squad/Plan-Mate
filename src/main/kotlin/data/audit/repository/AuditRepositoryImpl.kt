package data.audit.repository

import data.audit.datasource.AuditDataSource
import logic.model.Audit
import logic.repository.AuditRepository

class AuditRepositoryImpl(
    private val auditdataSource: AuditDataSource
) : AuditRepository {

    override suspend fun createAuditLog(auditLog: Audit) {
        auditdataSource.createAuditLog(auditLog)
    }

    override suspend fun getAuditByEntityId(entityId: String): List<Audit> {
        return auditdataSource.getAuditByEntityId(entityId)
    }
}