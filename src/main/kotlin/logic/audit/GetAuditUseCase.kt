
import squad.abudhabi.logic.exceptions.EmptyList
import squad.abudhabi.logic.exceptions.WrongInputException
import squad.abudhabi.logic.model.Audit
import squad.abudhabi.logic.repository.AuditRepository


class GetAuditUseCase(
    private val auditRepository: AuditRepository
){

    fun getAuditHistory(entityId: String): List<Audit> {

        if (entityId.isEmpty()) throw WrongInputException()
        if (auditRepository.getAuditByEntityId(entityId).isEmpty()) throw EmptyList()
        return auditRepository.getAuditByEntityId(entityId)
    }
}