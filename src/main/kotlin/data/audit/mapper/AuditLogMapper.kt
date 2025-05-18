package data.audit.mapper

import data.audit.model.AuditDto
import logic.model.Audit
import logic.model.Audit.EntityType
import java.time.ZoneId
import kotlin.uuid.Uuid
import java.util.Date
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
            date = Date.from(audit.createdAt.atZone(ZoneId.systemDefault()).toInstant())
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
            createdAt = dto.date
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
        )
    }
}