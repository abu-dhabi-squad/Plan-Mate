package logic.repository

import logic.model.Audit

interface AuditRepository {
    suspend fun createAuditLog(auditLog: Audit)
    suspend fun getAuditByEntityId(entityId: String): List<Audit>
}
