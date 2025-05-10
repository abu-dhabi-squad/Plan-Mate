package data.audit.datasource.csv.parser

import logic.model.Audit

interface AuditParser {
    fun getLineFromAudit(audit: Audit): String
    fun getAuditFromLine(auditLine: String): Audit
}