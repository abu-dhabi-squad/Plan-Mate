package data.audit.repository

import data.audit.datasource.AuditDataSource
import logic.model.Audit
import logic.repository.AuditRepository

class AuditRepositoryImpl(
    private val dataSource: AuditDataSource
) : AuditRepository {

    override fun createAuditLog(auditLog: Audit) {
        dataSource.createAuditLog(auditLog)
    }

    override fun getAuditByEntityId(entityId: String): List<Audit> {
        return dataSource.getAuditByEntityId(entityId)
    }
}