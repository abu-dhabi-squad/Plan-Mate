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
        if (!isContainEmptyValue(auditLog)&& !isSameStates(newState = auditLog.newState, oldState = auditLog.oldState))
            auditRepository.createAuditLog(auditLog)
        else throw InvalidAudit()
    }

    private fun isContainEmptyValue(auditLog: Audit): Boolean {
        return auditLog.newState.isBlank() || auditLog.createdBy.isBlank()
    }

    private fun isSameStates(newState: String, oldState: String) = newState == oldState
}
