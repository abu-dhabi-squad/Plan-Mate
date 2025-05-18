package logic.model

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.LocalDateTime
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
    val createdAt: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
){
    enum class EntityType {
        PROJECT, TASK
    }
}
