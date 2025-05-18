package helper

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import logic.model.Audit
import logic.model.Audit.EntityType
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun createAudit(
    id: Uuid = Uuid.random(),
    entityId: Uuid = Uuid.random(),
    newState: String = "",
    oldState: String = "",
    createdBy: String = "",
    date: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
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
