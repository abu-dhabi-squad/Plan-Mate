package data.audit.repository

import logic.model.Audit

interface LocalAuditDataSource {
    fun createAuditLog(audit: Audit)
    fun getAuditByEntityId(entityId: String): List<Audit>
}