package squad.abudhabi.logic.repository

import squad.abudhabi.logic.model.Audit

interface AuditRepository {
    suspend fun createAuditLog(auditLog: Audit)
    suspend fun getAuditByEntityId(entityId: String): List<Audit>
}
