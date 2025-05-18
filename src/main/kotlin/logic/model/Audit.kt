package logic.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.uuid.Uuid
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
data class Audit (
    val auditId: Uuid = Uuid.random(),
    val createdBy: String,
    val entityId: Uuid,
    val entityType: EntityType,
    val oldState: String,
    val newState: String,
    val createdAt: LocalDateTime = LocalDate.now().atTime(LocalTime.now()),
){
    enum class EntityType {
        PROJECT, TASK
    }
}
