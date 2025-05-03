import squad.abudhabi.logic.model.Audit
import squad.abudhabi.logic.model.EntityType
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID

fun createAudit(
    id: UUID = UUID.randomUUID(),
    entityId : String = "UG7299",
    newState: String = "InProgress",
    oldState : String = "TODO",
    createdBy: String = "UG",
    date: LocalDateTime = LocalDate.now().atTime(LocalTime.now()),
    entityType: EntityType = EntityType.PROJECT,
): Audit {
    return Audit(
        id = id,
        createdBy = createdBy,
        entityId = entityId,
        entityType = entityType,
        oldState = oldState,
        newState = newState,
        date = date
    )
}
