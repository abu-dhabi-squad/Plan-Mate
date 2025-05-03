package data.parser

import logic.validation.DateParser
import squad.abudhabi.logic.model.Audit
import squad.abudhabi.logic.model.EntityType

import java.util.UUID

class CsvAuditParser(
    private val dateParser: DateParser
): AuditParser {

    override fun getLineFromAudit(audit: Audit): String {
        return "${audit.id}," +
                "${audit.createdBy}," +
                "${audit.entityId}," +
                "${audit.entityType}," +
                "${audit.oldState}," +
                "${audit.newState}," +
                dateParser.getStringFromDate(audit.date)
    }

    override fun getAuditFromLine(auditLine: String): Audit {
        val parts = auditLine.split(",")

        if (parts.size < 7) {
            throw IllegalArgumentException("Invalid CSV format for Audit: $auditLine")
        }

        return Audit(
            id = UUID.fromString(parts[AuditColumnIndex.ID]),
            createdBy = parts[AuditColumnIndex.CREATED_BY],
            entityId = parts[AuditColumnIndex.ENTITY_ID],
            entityType = EntityType.valueOf(parts[AuditColumnIndex.ENTITY_TYPE]),
            oldState = parts[AuditColumnIndex.OLD_STATE],
            newState = parts[AuditColumnIndex.NEW_STATE],
            date = dateParser.parseDateFromString(parts[AuditColumnIndex.DATE]),
        )
    }
}
