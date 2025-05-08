package data.audit.mapper

import data.audit.model.AuditDto
import logic.model.Audit
import logic.model.EntityType
import java.time.ZoneId
import java.util.Date
import java.util.UUID

class AuditMapper {

    fun auditToDto(audit: Audit): AuditDto {
        return AuditDto(
            id = audit.id.toString(),
            createdBy = audit.createdBy,
            entityId = audit.entityId,
            entityType = audit.entityType.name,
            oldState = audit.oldState,
            newState = audit.newState,
            date = Date.from(audit.date.atZone(ZoneId.systemDefault()).toInstant())
        )
    }

    fun dtoToAudit(dto: AuditDto): Audit {
        return Audit(
            id = UUID.fromString(dto.id),
            createdBy = dto.createdBy,
            entityId = dto.entityId,
            entityType = EntityType.valueOf(dto.entityType),
            oldState = dto.oldState,
            newState = dto.newState,
            date = dto.date
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
        )
    }
}