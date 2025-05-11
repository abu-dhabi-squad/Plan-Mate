package data.audit.mapper

import data.audit.model.AuditDto
import logic.model.Audit
import logic.model.EntityType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import java.util.UUID

class AuditMapperTest {

    private val mapper = AuditMapper()

    @Test
    fun `should return valid AuditDto when mapping from Audit`() {
        // Given
        val uuid = UUID.randomUUID()
        val now = LocalDateTime.now()
        val audit = Audit(
            auditId = uuid,
            createdBy = "user123",
            entityId = "entity123",
            entityType = EntityType.TASK,
            oldState = "old",
            newState = "new",
            createdAt = now
        )

        // When
        val dto = mapper.auditToDto(audit)

        // Then
        assertEquals(uuid.toString(), dto.id)
        assertEquals("user123", dto.createdBy)
        assertEquals("entity123", dto.entityId)
        assertEquals("TASK", dto.entityType)
        assertEquals("old", dto.oldState)
        assertEquals("new", dto.newState)
        assertEquals(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()), dto.date)
    }

    @Test
    fun `should return valid Audit when mapping from AuditDto`() {
        // Given
        val uuid = UUID.randomUUID()
        val now = LocalDateTime.now()
        val date = Date.from(now.atZone(ZoneId.systemDefault()).toInstant())
        val dto = AuditDto(
            id = uuid.toString(),
            createdBy = "user456",
            entityId = "entity456",
            entityType = "PROJECT",
            oldState = "v1",
            newState = "v2",
            date = date
        )

        // When
        val audit = mapper.dtoToAudit(dto)

        // Then
        assertEquals(uuid, audit.auditId)
        assertEquals("user456", audit.createdBy)
        assertEquals("entity456", audit.entityId)
        assertEquals(EntityType.PROJECT, audit.entityType)
        assertEquals("v1", audit.oldState)
        assertEquals("v2", audit.newState)
    }
}
