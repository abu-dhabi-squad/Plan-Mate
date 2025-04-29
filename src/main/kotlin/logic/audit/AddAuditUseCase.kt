package squad.abudhabi.logic.audit

import squad.abudhabi.logic.exceptions.InvalidAudit
import squad.abudhabi.logic.model.Audit
import squad.abudhabi.logic.model.EntityType
import squad.abudhabi.logic.repository.AuditRepository
import java.time.LocalDate

class AddAuditUseCase(
    private val auditRepository: AuditRepository
) {
    fun addAudit(
        id: String,
        createdBy: String,
        entityId: String,
        entityType: EntityType,
        oldState: String,
        newState: String,
        date: LocalDate
    ){
        val auditLog = Audit(
            id = id,
            createdBy = createdBy,
            entityId = entityId,
            entityType = entityType,
            oldState = oldState,
            newState = newState,
            date = date,
        )

        if (auditLog.isValid() && isValidState(newState = newState, oldState = oldState))
        auditRepository.addAuditLog(auditLog)
        else throw InvalidAudit()
    }

    private fun Audit.isValid(): Boolean {
        return newState.isNotEmpty() &&
               id.isNotEmpty() &&
               entityId.isNotEmpty() &&
               newState.isNotEmpty()
    }

    private fun isValidState (newState: String, oldState: String) = newState != oldState
}
