package data.audit.repository

import data.audit.datasource.AuditDataSource
import squad.abudhabi.logic.model.Audit
import squad.abudhabi.logic.repository.AuditRepository

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