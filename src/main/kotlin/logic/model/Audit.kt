package squad.abudhabi.logic.model

import java.time.LocalDate

data class Audit(
    val id: String,
    val userId: String,
    val createdBy: String,
    val entityId: String,
    val oldState: String,
    val newState: String,
    val date: LocalDate,
    val entityType: EntityType
)
