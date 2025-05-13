package logic.audit

import logic.exceptions.NoAuditsFoundException
import logic.model.Audit
import logic.repository.AuditRepository
import java.util.*

class GetAuditUseCase(
    private val auditRepository: AuditRepository
) {
    suspend operator fun invoke(entityId: UUID): List<Audit> {
        return auditRepository.getAuditByEntityId(entityId).takeIf { it.isNotEmpty() } ?: throw NoAuditsFoundException()
    }
}