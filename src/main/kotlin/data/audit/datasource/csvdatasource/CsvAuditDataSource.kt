package data.audit.datasource.csvdatasource

import data.audit.datasource.csvdatasource.csvparser.AuditParser
import data.utils.filehelper.FileHelper
import logic.model.Audit

class CsvAuditDataSource(
    private val csvFileHelper: FileHelper,
    private val csvFileName: String,
    private val csvAuditParser: AuditParser,
) : LocalAuditDataSource {

    override  fun createAuditLog(audit: Audit) {
        return csvFileHelper.appendFile(csvFileName, listOf(csvAuditParser.getLineFromAudit(audit)))
    }

    override  fun getAuditByEntityId(entityId: String): List<Audit> {
        return csvFileHelper.readFile(csvFileName).map { csvAuditParser.getAuditFromLine(it) }.filter { audit ->
            audit.entityId == entityId
        }
    }
}