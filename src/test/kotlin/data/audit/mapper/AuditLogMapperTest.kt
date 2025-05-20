package data.audit.mapper

import com.google.common.truth.Truth.assertThat
import data.audit.model.AuditDto
import kotlinx.datetime.Clock
import logic.model.Audit
import logic.model.Audit.EntityType
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class AuditLogMapperTest {

    private val mapper = AuditLogMapper()

    @Test
    fun `should return valid AuditDto when mapping from Audit`() {
        // Given
        val uuid = Uuid.random()
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val audit = Audit(
            auditId = uuid,
            createdBy = "shahd",
            entityId = Uuid.parse("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"),
            entityType = EntityType.TASK,
            oldState = "old",
            newState = "new",
            createdAt = now
        )

        // When
        val dto = mapper.auditToDto(audit)

        // Then
        assertThat(dto._id).isEqualTo(uuid.toString())
        assertThat(dto.createdBy).isEqualTo("shahd")
        assertThat(dto.entityId).isEqualTo("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a")
        assertThat(dto.entityType).isEqualTo("TASK")
        assertThat(dto.oldState).isEqualTo("old")
        assertThat(dto.newState).isEqualTo("new")
        assertThat(dto.date).isEqualTo(now.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds())
    }

    @Test
    fun `should return valid Audit when mapping from AuditDto`() {
        // Given
        val uuid = Uuid.random()
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val date = now.toInstant(TimeZone.currentSystemDefault()).epochSeconds
        val dto = AuditDto(
            _id = uuid.toString(),
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
        assertThat(audit.entityId).isEqualTo(Uuid.parse("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"))
        assertThat(audit.entityType).isEqualTo(EntityType.PROJECT)
        assertThat(audit.oldState).isEqualTo("s1")
        assertThat(audit.newState).isEqualTo("s2")
    }

    @Test
    fun `should throw exception when invalid AuditDto is passed`() {
        // Given
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val invalidDto = AuditDto(
            _id = "invalid-uuid",
            createdBy = "shahd",
            entityId = "not-a-valid-uuid",
            entityType = "INVALID_TYPE",
            oldState = "s1",
            newState = "s2",
            date = now.toInstant(TimeZone.currentSystemDefault()).epochSeconds
        )

        // When & Then
        assertThrows<Exception> {
            mapper.dtoToAudit(invalidDto)
        }
    }
}