import squad.abudhabi.logic.model.Audit
import squad.abudhabi.logic.model.EntityType
import java.time.LocalDate

fun createAudit(
    id: String,
    entityId : String,
): Audit {
    return Audit(
        id = "0",
        createdBy = "dsdsa",
        entityId = "task123",
        entityType = EntityType.TASK,
        oldState = "TODO",
        newState = "InProgress",
        date = LocalDate.now()
    )
}
