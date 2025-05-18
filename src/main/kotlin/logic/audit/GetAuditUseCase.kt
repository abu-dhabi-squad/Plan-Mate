package logic.audit

import logic.exceptions.NoAuditsFoundException
import logic.model.Audit
import logic.repository.AuditRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class GetAuditUseCase(
    private val auditRepository: AuditRepository
) {
    suspend operator fun invoke(entityId: Uuid): List<Audit> {
        return auditRepository.getAuditByEntityId(entityId).takeIf { it.isNotEmpty() } ?: throw NoAuditsFoundException()
    }
}