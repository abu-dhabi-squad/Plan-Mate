package data.audit.datasource

import com.google.common.truth.Truth
import createAudit
import data.audit.datasource.csvdatasource.CsvAuditDataSource
import data.audit.datasource.csvdatasource.LocalAuditDataSource
import data.audit.datasource.csvdatasource.csvparser.CsvAuditParser
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import data.utils.filehelper.CsvFileHelper
import kotlinx.coroutines.test.runTest
import kotlin.test.assertFails
import kotlin.test.assertTrue

class CsvAuditDataSourceTest {

    private lateinit var csvFileHelper: CsvFileHelper
    private lateinit var csvAuditParser: CsvAuditParser
    private lateinit var csvAuditDataSource: LocalAuditDataSource

    @BeforeEach
    fun setup() {
        csvFileHelper = mockk(relaxed = true)
        csvAuditParser = mockk(relaxed = true)
        csvAuditDataSource = CsvAuditDataSource(csvFileHelper, FILE_NAME, csvAuditParser)
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
    fun `addAudit should rethrow exception when file throw exception`() {
        // given
        val audit = createAudit()
        every { csvFileHelper.appendFile(any(), any()) } throws Exception()
        // when & then
        assertFails { csvAuditDataSource.createAuditLog(audit) }
    }


    @Test
    fun `getAuditByEntityId should return list of audits that match entity id`() {
        // given
        val audits = listOf(
            createAudit(),
            createAudit()
        )
        every { csvFileHelper.readFile(any()) } returns audits.map { csvAuditParser.getLineFromAudit(it) }
        every { csvAuditParser.getAuditFromLine(any()) } returnsMany audits
        // when
        val result = csvAuditDataSource.getAuditByEntityId(ENTITY_ID)
        // then
        Truth.assertThat(result).containsExactly(*audits.toTypedArray())
    }


    @Test
    fun `getAuditByEntityId should return empty list when file is empty`() {
        // given
        every { csvFileHelper.readFile(any()) } returns emptyList()
        // when & then
        assertTrue { csvAuditDataSource.getAuditByEntityId(ENTITY_ID).isEmpty() }
    }

    @Test
    fun `getAuditByEntityId should rethrow exception when file throws Exception`() {
        // given
        every { csvFileHelper.readFile(any()) } throws Exception()

        // when & then
        assertFails { csvAuditDataSource.getAuditByEntityId(ENTITY_ID) }
    }

    companion object {
        private const val FILE_NAME = "build/audit_system_test.csv"
        private const val ENTITY_ID = "UG7299"
    }
}