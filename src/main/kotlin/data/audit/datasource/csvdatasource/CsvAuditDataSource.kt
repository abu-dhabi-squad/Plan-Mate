package data.audit.datasource.csvdatasource

import data.audit.datasource.AuditDataSource
import data.parser.AuditParser
import squad.abudhabi.data.utils.filehelper.FileHelper
import squad.abudhabi.logic.model.Audit

class CsvAuditDataSource(
    private val csvFileHelper: FileHelper,
    private val csvFileName: String,
    private val csvAuditParser: AuditParser,
) : AuditDataSource {

    override suspend fun createAuditLog(audit: Audit) {
        return csvFileHelper.appendFile(csvFileName, listOf(csvAuditParser.getLineFromAudit(audit)))
    }

    override suspend fun getAuditByEntityId(entityId: String): List<Audit> {
        return csvFileHelper.readFile(csvFileName).map { csvAuditParser.getAuditFromLine(it) }.filter { audit ->
            audit.entityId == entityId
        }
    }
}