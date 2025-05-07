package data.audit.mapper

import com.google.common.truth.Truth.assertThat
import logic.model.Audit
import logic.model.EntityType
import org.bson.Document
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

class AuditMapperTest {
    private lateinit var auditMapper: AuditMapper

    @BeforeEach
    fun setup() {
        auditMapper = AuditMapper()
    }

    private val sampleAudit = Audit(
        id = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
        createdBy = "user123",
        entityId = "entity456",
        entityType = EntityType.PROJECT,
        oldState = "{\"name\":\"Old Name\"}",
        newState = "{\"name\":\"New Name\"}",
        date = LocalDateTime.of(2023, 5, 1, 14, 30)
    )

    @Test
    fun `should convert Audit to Document correctly`() {
        // when
        val document = auditMapper.auditToDocument(sampleAudit)

        // when & then
        assertThat(document.getString("id")).isEqualTo("123e4567-e89b-12d3-a456-426614174000")
        assertThat(document.getString("createdBy")).isEqualTo("user123")
        assertThat(document.getString("entityId")).isEqualTo("entity456")
        assertThat(document.getString("entityType")).isEqualTo("PROJECT")
        assertThat(document.getString("oldState")).isEqualTo("{\"name\":\"Old Name\"}")
        assertThat(document.getString("newState")).isEqualTo("{\"name\":\"New Name\"}")
        assertThat(document.getString("date")).isEqualTo("2023-05-01T14:30")
    }

    @Test
    fun `should convert Document to Audit correctly`() {
        // given
        val document = Document().apply {
            append("id", "123e4567-e89b-12d3-a456-426614174000")
            append("createdBy", "user123")
            append("entityId", "entity456")
            append("entityType", "PROJECT")
            append("oldState", "{\"name\":\"Old Name\"}")
            append("newState", "{\"name\":\"New Name\"}")
            append("date", "2023-05-01T14:30")
        }

        // when
        val audit = auditMapper.documentToAudit(document)

        // then
        assertThat(audit.id).isEqualTo(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
        assertThat(audit.createdBy).isEqualTo("user123")
        assertThat(audit.entityId).isEqualTo("entity456")
        assertThat(audit.entityType).isEqualTo(EntityType.PROJECT)
        assertThat(audit.oldState).isEqualTo("{\"name\":\"Old Name\"}")
        assertThat(audit.newState).isEqualTo("{\"name\":\"New Name\"}")
        assertThat(audit.date).isEqualTo(LocalDateTime.of(2023, 5, 1, 14, 30))
    }
}
