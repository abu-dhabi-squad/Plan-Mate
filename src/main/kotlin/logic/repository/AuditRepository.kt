package squad.abudhabi.logic.repository

import squad.abudhabi.logic.model.Audit

interface AuditRepository {
    fun saveAuditLog(auditLog: Audit)
    fun getAuditByEntityId(entityId: String): List<Audit>
}
