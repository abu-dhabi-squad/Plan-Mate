package squad.abudhabi.logic.audit

import squad.abudhabi.logic.exceptions.WrongInputException
import squad.abudhabi.logic.model.Audit
import squad.abudhabi.logic.repository.AuditRepository

class GetAuditUseCase(private val auditRepository: AuditRepository){

    fun getAuditHistory(entityId: String): List<Audit> {

        if (entityId.isNotEmpty())
        return auditRepository.getAuditByEntityId(entityId)
        else throw WrongInputException()
    }
}