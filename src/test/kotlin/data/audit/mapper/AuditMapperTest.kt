package data.audit.mapper

import com.google.common.truth.Truth.assertThat
import data.audit.model.AuditDto
import logic.model.Audit
import logic.model.EntityType
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
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
            createdBy = "shahd",
            entityId = UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"),
            entityType = EntityType.TASK,
            oldState = "old",
            newState = "new",
            createdAt = now
        )

        // When
        val dto = mapper.auditToDto(audit)

        // Then
        assertThat(dto.id).isEqualTo(uuid.toString())
        assertThat(dto.createdBy).isEqualTo("shahd")
        assertThat(dto.entityId).isEqualTo("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a")
        assertThat(dto.entityType).isEqualTo("TASK")
        assertThat(dto.oldState).isEqualTo("old")
        assertThat(dto.newState).isEqualTo("new")
        assertThat(dto.date).isEqualTo(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
    }

    @Test
    fun `should return valid Audit when mapping from AuditDto`() {
        // Given
        val uuid = UUID.randomUUID()
        val now = LocalDateTime.now()
        val date = Date.from(now.atZone(ZoneId.systemDefault()).toInstant())
        val dto = AuditDto(
            id = uuid.toString(),
            createdBy = "shahd",
            entityId = "d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a",
            entityType = "PROJECT",
            oldState = "s1",
            newState = "s2",
            date = date
        )

        // When
        val audit = mapper.dtoToAudit(dto)

        // Then
        assertThat(audit.auditId).isEqualTo(uuid)
        assertThat(audit.createdBy).isEqualTo("shahd")
        assertThat(audit.entityId).isEqualTo(UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"))
        assertThat(audit.entityType).isEqualTo(EntityType.PROJECT)
        assertThat(audit.oldState).isEqualTo("s1")
        assertThat(audit.newState).isEqualTo("s2")
    }

    @Test
    fun `should throw exception when invalid AuditDto is passed`() {
        // Given
        val invalidDto = AuditDto(
            id = "invalid-uuid",
            createdBy = "shahd",
            entityId = "not-a-valid-uuid",
            entityType = "INVALID_TYPE",
            oldState = "s1",
            newState = "s2",
            date = Date()
        )

        // When & Then
        assertThrows<Exception> {
            mapper.dtoToAudit(invalidDto)
        }
    }
}