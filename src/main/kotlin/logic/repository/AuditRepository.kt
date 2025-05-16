package logic.repository

import logic.model.Audit
import java.util.*

interface AuditRepository {
    suspend fun createAuditLog(auditLog: Audit)
    suspend fun getAuditByEntityId(entityId: UUID): List<Audit>
}
