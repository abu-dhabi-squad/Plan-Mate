package logic.repository

import logic.model.Audit
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface AuditRepository {
    suspend fun createAuditLog(auditLog: Audit)
    suspend fun getAuditByEntityId(entityId: Uuid): List<Audit>
}
