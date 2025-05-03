package data.parser

import squad.abudhabi.logic.model.Audit

interface AuditParser {
    fun getLineFromAudit(audit: Audit): String
    fun getAuditFromLine(auditLine: String): Audit
}