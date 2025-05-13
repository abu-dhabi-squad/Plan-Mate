package data.audit.datasource.csv.parser

import logic.model.Audit
import logic.model.EntityType
import logic.utils.DateTimeParser
import java.util.UUID

class CsvAuditParser(
    private val dateParser: DateTimeParser
) {

    fun getLineFromAudit(audit: Audit): String {
        return "${audit.auditId}," +
                "${audit.createdBy}," +
                "${audit.entityId}," +
                "${audit.entityType}," +
                "${audit.oldState}," +
                "${audit.newState}," +
                dateParser.getStringFromDate(audit.createdAt)
    }

    fun getAuditFromLine(auditLine: String): Audit {
        val parts = auditLine.split(",")

        if (parts.size < 7) {
            throw IllegalArgumentException("Invalid CSV format for Audit: $auditLine")
        }

        return Audit(
            auditId = UUID.fromString(parts[ID]),
            createdBy = parts[CREATED_BY],
            entityId = UUID.fromString(parts[ENTITY_ID]),
            entityType = EntityType.valueOf(parts[ENTITY_TYPE]),
            oldState = parts[OLD_STATE],
            newState = parts[NEW_STATE],
            createdAt = dateParser.parseDateFromString(parts[DATE]),
        )
    }

    private companion object {
        const val ID = 0
        const val CREATED_BY = 1
        const val ENTITY_ID = 2
        const val ENTITY_TYPE = 3
        const val OLD_STATE = 4
        const val NEW_STATE = 5
        const val DATE = 6
    }
}
