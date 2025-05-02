package logic.audit

import squad.abudhabi.logic.exceptions.InvalidAudit
import squad.abudhabi.logic.model.Audit
import squad.abudhabi.logic.repository.AuditRepository

class CreateAuditUseCase(
    private val auditRepository: AuditRepository
) {
    fun createAuditLog(
        auditLog: Audit
    ){

        if (auditLog.isValid() && !isSameStates(newState = auditLog.newState, oldState = auditLog.oldState))
        auditRepository.createAuditLog(auditLog)
        else throw InvalidAudit()
    }

    private fun Audit.isValid(): Boolean {
        return newState.isNotEmpty() &&
               entityId.isNotEmpty() &&
               createdBy.isNotEmpty()
    }

    private fun isSameStates (newState: String, oldState: String) = newState == oldState
}
