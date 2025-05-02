package squad.abudhabi.data.audit.datasource

import squad.abudhabi.logic.model.Audit

interface AuditDataSource {

    fun addAuditLog(audit: Audit)
    fun getAuditByEntityId(entityId: String) : List<Audit>
}