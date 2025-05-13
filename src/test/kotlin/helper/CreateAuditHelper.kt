package helper

import logic.model.Audit
import logic.model.EntityType
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID

fun createAudit(
    id: UUID = UUID.randomUUID(),
    entityId: UUID = UUID.randomUUID(),
    newState: String = "",
    oldState : String = "",
    createdBy: String = "",
    date: LocalDateTime = LocalDate.now().atTime(LocalTime.now()),
    entityType: EntityType = EntityType.PROJECT,
): Audit {
    return Audit(
        auditId = id,
        createdBy = createdBy,
        entityId = entityId,
        entityType = entityType,
        oldState = oldState,
        newState = newState,
        createdAt = date
    )
}
