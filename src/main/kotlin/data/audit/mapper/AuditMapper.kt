package data.audit.mapper

import data.audit.model.AuditDto
import logic.model.Audit
import logic.model.EntityType
import java.time.ZoneId
import java.util.*

class AuditMapper {
    fun auditToDto(audit: Audit): AuditDto {
        return AuditDto(
            id = audit.auditId.toString(),
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
            auditId = UUID.fromString(dto.id),
            createdBy = dto.createdBy,
            entityId = UUID.fromString(dto.entityId),
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