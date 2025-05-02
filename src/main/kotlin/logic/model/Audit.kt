package squad.abudhabi.logic.model

import java.time.LocalDate
import java.util.UUID

data class Audit(
    val id: UUID = UUID.randomUUID(),
    val createdBy: String,
    val entityId: String,
    val entityType: EntityType,
    val oldState: String,
    val newState: String,
    val date: LocalDate,
)
