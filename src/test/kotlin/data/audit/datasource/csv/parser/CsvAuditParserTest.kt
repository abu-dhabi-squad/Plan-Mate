package data.audit.datasource.csv.parser

import io.mockk.every
import io.mockk.mockk
import logic.validation.DateTimeParser
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import logic.model.Audit
import logic.model.EntityType
import java.time.LocalDate
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertFails

class CsvAuditParserTest {

    lateinit var dateParser: DateTimeParser
    lateinit var csvAuditParser: AuditParser


    @BeforeEach
    fun setup(){
        dateParser = mockk(relaxed = true)
        csvAuditParser = CsvAuditParser(dateParser)
    }

    @Test
    fun `getLineFromAudit should return CSV line correctly`() {

        // given
        val id = UUID.randomUUID()
        val customDateTime = LocalDate.of(2025, 5, 24).atTime(20, 0)
        val audit =   Audit(
            id = id,
            createdBy = "user123",
            entityId = "42",
            entityType = EntityType.PROJECT,
            oldState = "old",
            newState = "new",
            date =customDateTime
        )
        every { dateParser.getStringFromDate(audit.date) } returns "2025-05-24 08:00 PM"

        // when
        val result = csvAuditParser.getLineFromAudit(audit)

        // then
        val expected = "$id,${audit.createdBy},${audit.entityId},${audit.entityType},${audit.oldState},${audit.newState},2025-05-24 08:00 PM"
        assertEquals(expected, result)
    }

    @Test
    fun `getAuditFromLine should parse CSV line into Audit object`() {

        // given
        val id = UUID.randomUUID()
        val line = "$id,user123,42,PROJECT,old,new,2023-12-25"
        val customDateTime = LocalDate.of(2025, 5, 24).atTime(20, 0)

        every { dateParser.parseDateFromString("2023-12-25") } returns customDateTime

        // when
        val result = csvAuditParser.getAuditFromLine(line)

        // then
        val expectedAudit = Audit(
            id = id,
            createdBy = "user123",
            entityId = "42",
            entityType = EntityType.PROJECT,
            oldState = "old",
            newState = "new",
            date = customDateTime
        )

        assertEquals(expectedAudit, result)
    }

    @Test
    fun `getAuditFromLine should throw exception for invalid CSV line`() {

        // given
        val invalidLine = "1,user123,42,PROJECT,old"

        // when & then
        assertFails {
            csvAuditParser.getAuditFromLine(invalidLine)
        }
    }

}