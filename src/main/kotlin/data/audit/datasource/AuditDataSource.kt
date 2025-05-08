package data.audit.datasource

import logic.model.Audit

interface AuditDataSource {

    fun createAuditLog(audit: Audit)
    fun getAuditByEntityId(entityId: String) : List<Audit>
}