package squad.abudhabi.logic.model

data class Audit(
    val id: String,
    val userId: String,
    val createdBy: String,
    val entityId: String,
    val entityType: EntityType,
    val oldState: String,
    val newState: String,
    val date: String,
)
