package squad.abudhabi.data.audit.datasource

import squad.abudhabi.logic.model.Audit

class CsvAuditDataSource(
//    private val csvFileHelper: CsvFileHelper,
//    private val csvFileName: String,
//    private val csvAuditParser: CsvTaskParser,
) : AuditDataSource{

    override fun addAuditLog(audit: Audit) {
        TODO("Not yet implemented")
    }

    override fun getAuditHistory(entityId: String): List<Audit> {
        TODO("Not yet implemented")
    }

}