package logic.audit

import squad.abudhabi.logic.exceptions.EmptyList
import squad.abudhabi.logic.exceptions.WrongInputException
import squad.abudhabi.logic.model.Audit
import squad.abudhabi.logic.repository.AuditRepository

class GetAuditUseCase(
    private val auditRepository: AuditRepository
){
    suspend operator fun invoke(entityId: String): List<Audit> {

        if (entityId.isEmpty()) throw WrongInputException()
        return auditRepository.getAuditByEntityId(entityId).takeIf { it.isNotEmpty() } ?: throw EmptyList()
    }
}