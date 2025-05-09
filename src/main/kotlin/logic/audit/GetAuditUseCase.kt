package logic.audit

import logic.exceptions.EmptyList
import logic.exceptions.WrongInputException
import logic.model.Audit
import logic.repository.AuditRepository

class GetAuditUseCase(
    private val auditRepository: AuditRepository
){
    suspend operator fun invoke(entityId: String): List<Audit> {
        if (entityId.trim().isEmpty()) throw WrongInputException()
        return auditRepository.getAuditByEntityId(entityId).takeIf { it.isNotEmpty() } ?: throw EmptyList()
    }
}