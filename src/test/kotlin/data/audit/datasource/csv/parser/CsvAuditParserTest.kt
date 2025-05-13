package data.audit.datasource.csv.parser

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import presentation.logic.utils.DateTimeParser
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import logic.model.Audit
import logic.model.EntityType
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import java.util.UUID

class CsvAuditParserTest {

    lateinit var dateParser: DateTimeParser
    lateinit var csvAuditParser: CsvAuditParser


    @BeforeEach
    fun setup() {
        dateParser = mockk(relaxed = true)
        csvAuditParser = CsvAuditParser(dateParser)
    }

    @Test
    fun `getLineFromAudit should return CSV line correctly`() {

        // given
        val id = UUID.randomUUID()
        val customDateTime = LocalDate.of(2025, 5, 24).atTime(20, 0)
        val audit = Audit(
            auditId = id,
            createdBy = "user123",
            entityId = UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"),
            entityType = EntityType.PROJECT,
            oldState = "old",
            newState = "new",
            createdAt = customDateTime
        )
        every { dateParser.getStringFromDate(audit.createdAt) } returns "2025-05-24 08:00 PM"

        // when
        val result = csvAuditParser.getLineFromAudit(audit)

        // then
        val expected =
            "$id,${audit.createdBy},${audit.entityId},${audit.entityType},${audit.oldState},${audit.newState},2025-05-24 08:00 PM"
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getAuditFromLine should parse CSV line into Audit object`() {

        // given
        val id = UUID.randomUUID()
        val line = "$id,user123,d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a,PROJECT,old,new,2023-12-25"
        val customDateTime = LocalDate.of(2025, 5, 24).atTime(20, 0)

        every { dateParser.parseDateFromString("2023-12-25") } returns customDateTime

        // when
        val result = csvAuditParser.getAuditFromLine(line)

        // then
        val expectedAudit = Audit(
            auditId = id,
            createdBy = "user123",
            entityId = UUID.fromString("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a"),
            entityType = EntityType.PROJECT,
            oldState = "old",
            newState = "new",
            createdAt = customDateTime
        )

        assertThat(result).isEqualTo(expectedAudit)
    }


    @Test
    fun `getAuditFromLine should throw exception for invalid CSV line`() {

        // given
        val invalidLine = "1,user123,42,PROJECT,old"

        // when & then
        val exception = assertThrows<IllegalArgumentException> {
            csvAuditParser.getAuditFromLine(invalidLine)
        }

        // then
        assertThat(exception).hasMessageThat().contains("Invalid CSV")
    }
}