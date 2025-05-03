package data.parser

import io.mockk.every
import io.mockk.mockk
import logic.validation.DateParser
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import squad.abudhabi.logic.model.Audit
import squad.abudhabi.logic.model.EntityType
import java.time.LocalDate
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertFails

class CsvAuditParserTest {

    lateinit var dateParser: DateParser
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
        val audit = Audit(
            id = id,
            createdBy = "user123",
            entityId = "42",
            entityType = EntityType.PROJECT,
            oldState = "old",
            newState = "new",
            date = LocalDate.of(1999, 12, 20)
        )
        every { dateParser.getStringFromDate(audit.date) } returns "1999-12-20"

        // when
        val result = csvAuditParser.getLineFromAudit(audit)

        // then
        val expected = "$id,user123,42,PROJECT,old,new,1999-12-20"
        assertEquals(expected, result)
    }

    @Test
    fun `getAuditFromLine should parse CSV line into Audit object`() {

        // given
        val id = UUID.randomUUID()
        val line = "$id,user123,42,PROJECT,old,new,2023-12-25"
        val expectedDate = LocalDate.of(2023, 12, 25)

        every { dateParser.parseDateFromString("2023-12-25") } returns expectedDate

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
            date = expectedDate
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