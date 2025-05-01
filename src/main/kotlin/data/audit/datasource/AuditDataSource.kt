package squad.abudhabi.data.audit.datasource

import squad.abudhabi.logic.model.Audit

interface AuditDataSource {

    fun addAuditLog(audit: Audit)
    fun getAuditHistory(entityId: String) : List<Audit>
}