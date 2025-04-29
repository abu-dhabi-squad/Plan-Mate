package squad.abudhabi.logic.model

import java.time.LocalDate

data class Audit(
    val id: String,
    val createdBy: String,
    val entityId: String,
    val entityType: EntityType,
    val oldState: String,
    val newState: String,
    val date: LocalDate,
)
