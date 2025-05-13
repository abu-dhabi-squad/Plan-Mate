package logic.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID

data class Audit(
    val auditId: UUID = UUID.randomUUID(),
    val createdBy: String,
    val entityId: UUID,
    val entityType: EntityType,
    val oldState: String,
    val newState: String,
    val createdAt: LocalDateTime = LocalDate.now().atTime(LocalTime.now()),
)
