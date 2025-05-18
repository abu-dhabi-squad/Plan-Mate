package helper

import logic.model.Audit
import logic.model.Audit.EntityType
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun createAudit(
    id: Uuid = Uuid.random(),
    entityId: Uuid = Uuid.random(),
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
