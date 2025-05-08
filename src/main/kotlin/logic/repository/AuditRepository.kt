package logic.repository

import logic.model.Audit

interface AuditRepository {
    fun createAuditLog(auditLog: Audit)
    fun getAuditByEntityId(entityId: String): List<Audit>
}
