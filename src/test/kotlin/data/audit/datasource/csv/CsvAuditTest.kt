package data.audit.datasource.csv

import com.google.common.truth.Truth.assertThat
import data.audit.datasource.csv.parser.CsvAuditParser
import data.audit.repository.LocalAuditDataSource
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import data.utils.filehelper.CsvFileHelper
import helper.createAudit
import org.junit.jupiter.api.assertThrows
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class CsvAuditTest {

    private lateinit var csvFileHelper: CsvFileHelper
    private lateinit var csvAuditParser: CsvAuditParser
    private lateinit var csvAuditDataSource: LocalAuditDataSource

    @BeforeEach
    fun setup() {
        csvFileHelper = mockk(relaxed = true)
        csvAuditParser = mockk(relaxed = true)
        csvAuditDataSource = CsvAudit(csvFileHelper, "", csvAuditParser)
    }

    @Test
    fun `addAudit should call appendFile once when new audit is added successfully`() {
        // given
        val audit = createAudit()

        // when
        csvAuditDataSource.createAuditLog(audit)

        // then
        verify(exactly = 1) { csvFileHelper.appendFile(any(), any()) }
    }

    @Test
    fun `addAudit should rethrow exception when file throws exception`() {
        // given
        val audit = createAudit()
        every { csvFileHelper.appendFile(any(), any()) } throws Exception()

        // when & then
        assertThrows<Exception> {
            csvAuditDataSource.createAuditLog(audit)
        }
    }

    @Test
    fun `getAuditByEntityId should return list of audits that match entity id`() {
        // given
        val audits = listOf(
            createAudit(entityId = ENTITY_ID),
            createAudit(entityId = ENTITY_ID)
        )
        every { csvFileHelper.readFile(any()) } returns audits.map { csvAuditParser.getLineFromAudit(it) }
        every { csvAuditParser.getAuditFromLine(any()) } returnsMany audits

        // when
        val result = csvAuditDataSource.getAuditByEntityId(ENTITY_ID.toString())

        // then
        assertThat(result).isEqualTo(audits)

    }

    @Test
    fun `getAuditByEntityId should return empty list when file is empty`() {
        // given
        every { csvFileHelper.readFile(any()) } returns emptyList()

        // when
        val result = csvAuditDataSource.getAuditByEntityId(ENTITY_ID.toString())

        // then
        assertThat(result).isEmpty()
    }

    @Test
    fun `getAuditByEntityId should rethrow exception when file throws exception`() {
        // given
        every { csvFileHelper.readFile(any()) } throws Exception()

        // when & then
        assertThrows<Exception> {
            csvAuditDataSource.getAuditByEntityId(ENTITY_ID.toString())
        }
    }

    companion object {
        private val ENTITY_ID = Uuid.parse("d3b07384-d9a0-4e9f-8a1e-6f0c2e5c9b1a")
    }
}
