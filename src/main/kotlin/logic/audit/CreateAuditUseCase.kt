package logic.audit

import logic.exceptions.InvalidAudit
import logic.model.Audit
import logic.repository.AuditRepository

class CreateAuditUseCase(
    private val auditRepository: AuditRepository
) {
    suspend operator fun invoke(
        auditLog: Audit
    ) {

        if (auditLog.isValid() && !isSameStates(newState = auditLog.newState, oldState = auditLog.oldState))
            auditRepository.createAuditLog(auditLog)
        else throw InvalidAudit()
    }

    private fun Audit.isValid(): Boolean {
        return newState.isNotEmpty() && createdBy.isNotEmpty()
    }

    private fun isSameStates(newState: String, oldState: String) = newState == oldState
}
