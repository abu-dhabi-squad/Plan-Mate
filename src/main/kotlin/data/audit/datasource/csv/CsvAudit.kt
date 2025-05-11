package data.audit.datasource.csv

import data.audit.datasource.csv.parser.AuditParser
import data.audit.repository.LocalAuditDataSource
import data.utils.filehelper.FileHelper
import logic.model.Audit

class CsvAudit(
    private val csvFileHelper: FileHelper,
    private val csvFileName: String,
    private val csvAuditParser: AuditParser,
) : LocalAuditDataSource {

    override fun createAuditLog(audit: Audit) {
        return csvFileHelper.appendFile(csvFileName, listOf(csvAuditParser.getLineFromAudit(audit)))
    }

    override fun getAuditByEntityId(entityId: String): List<Audit> {
        return csvFileHelper.readFile(csvFileName).map { csvAuditParser.getAuditFromLine(it) }.filter { audit ->
            audit.entityId.toString() == entityId
        }
    }
}