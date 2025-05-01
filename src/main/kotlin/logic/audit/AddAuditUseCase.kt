package logic.audit

import squad.abudhabi.logic.exceptions.InvalidAudit
import squad.abudhabi.logic.model.Audit
import squad.abudhabi.logic.repository.AuditRepository

class AddAuditUseCase(
    private val auditRepository: AuditRepository
) {
    fun addAudit(
        auditLog: Audit
    ){

        if (auditLog.isValid() && !isSameStates(newState = auditLog.newState, oldState = auditLog.oldState))
        auditRepository.addAuditLog(auditLog)
        else throw InvalidAudit()
    }

    private fun Audit.isValid(): Boolean {
        return newState.isNotEmpty() &&
               entityId.isNotEmpty() &&
               createdBy.isNotEmpty()
    }

    private fun isSameStates (newState: String, oldState: String) = newState == oldState
}
