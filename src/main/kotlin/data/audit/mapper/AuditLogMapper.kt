package data.audit.mapper

import data.audit.model.AuditDto
import kotlinx.datetime.Instant
import logic.model.Audit
import logic.model.Audit.EntityType
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.uuid.Uuid
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class AuditLogMapper {

    fun auditToDto(audit: Audit): AuditDto {
        return AuditDto(
            _id = audit.auditId.toString(),
            createdBy = audit.createdBy,
            entityId = audit.entityId.toString(),
            entityType = audit.entityType.name,
            oldState = audit.oldState,
            newState = audit.newState,
            date = audit.createdAt.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
        )
    }

    fun dtoToAudit(dto: AuditDto): Audit {
        return Audit(
            auditId = Uuid.parse(dto._id),
            createdBy = dto.createdBy,
            entityId = Uuid.parse(dto.entityId),
            entityType = EntityType.valueOf(dto.entityType),
            oldState = dto.oldState,
            newState = dto.newState,
            createdAt = Instant.fromEpochMilliseconds(dto.date).toLocalDateTime(TimeZone.currentSystemDefault())
        )
    }
}
