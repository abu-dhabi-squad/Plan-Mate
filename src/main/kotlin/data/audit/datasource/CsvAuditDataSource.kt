package squad.abudhabi.data.audit.datasource

import data.parser.CsvAuditParser
import data.utils.filehelper.CsvFileHelper
import squad.abudhabi.logic.model.Audit

class CsvAuditDataSource(
    private val csvFileHelper: CsvFileHelper,
    private val csvFileName: String,
    private val csvAuditParser: CsvAuditParser,
) : AuditDataSource{

    override fun addAuditLog(audit: Audit) {
        return csvFileHelper.appendFile(csvFileName, listOf(csvAuditParser.getLineFromAudit(audit)))
    }

    override fun getAuditByEntityId(entityId: String): List<Audit> {
        return csvFileHelper.readFile(csvFileName).map { csvAuditParser.getAuditFromLine(it) }.filter { audit ->
            audit.entityId == entityId
        }
    }
}