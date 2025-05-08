package data.audit.datasource.csvdatasource

import logic.model.Audit

interface LocalAuditDataSource {

     fun createAuditLog(audit: Audit)
     fun getAuditByEntityId(entityId: String) : List<Audit>
}