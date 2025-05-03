package data.audit.repository

import squad.abudhabi.data.audit.datasource.AuditDataSource
import squad.abudhabi.logic.model.Audit
import squad.abudhabi.logic.repository.AuditRepository

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