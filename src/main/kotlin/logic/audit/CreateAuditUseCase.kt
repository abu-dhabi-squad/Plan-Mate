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

        if (isValid(auditLog.newState, auditLog.createdBy)&& !isSameStates(newState = auditLog.newState, oldState = auditLog.oldState))
            auditRepository.createAuditLog(auditLog)
        else throw InvalidAudit()
    }

    private fun isValid(newState: String, createdBy: String): Boolean {
        return newState.isNotEmpty() && createdBy.isNotEmpty()
    }

    private fun isSameStates(newState: String, oldState: String) = newState == oldState
}
