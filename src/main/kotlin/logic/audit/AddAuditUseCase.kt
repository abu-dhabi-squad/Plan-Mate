package squad.abudhabi.logic.audit

import squad.abudhabi.logic.model.Audit
import squad.abudhabi.logic.model.EntityType
import squad.abudhabi.logic.repository.AuditRepository

class AuditUseCase(
    private val auditRepository: AuditRepository
){

    fun addAudit(
        id: String,
        createdBy: String,
        date: String,
        userId: String,
        entityId: String,
        entityType: EntityType,
        oldState: String,
        newState: String,
    ) {
        val auditLog = Audit(
            id = id,
            createdBy = createdBy,
            date = date,
            userId = userId,
            entityId = entityId,
            entityType = entityType,
            oldState = oldState,
            newState = newState,
        )
        auditRepository.saveAuditLog(auditLog)
    }
}