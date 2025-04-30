import squad.abudhabi.logic.model.Audit
import squad.abudhabi.logic.model.EntityType
import java.time.LocalDate

fun createAudit(
    id: String,
    entityId : String,
    newState: String = "InProgress",
    oldState : String = "TODO",
    createdBy: String = "UG",
    entityType: EntityType = EntityType.TASK,
): Audit {
    return Audit(
        id = id,
        createdBy = createdBy,
        entityId = entityId,
        entityType = entityType,
        oldState = oldState,
        newState = newState,
        date = LocalDate.now()
    )
}
