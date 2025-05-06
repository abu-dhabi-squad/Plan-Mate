package data.audit.auditmapper

import data.audit.auditmapper.AuditMapperFields.CREATED_BY_FIELD
import data.audit.auditmapper.AuditMapperFields.DATE_FIELD
import data.audit.auditmapper.AuditMapperFields.ENTITY_ID_FIELD
import data.audit.auditmapper.AuditMapperFields.ENTITY_TYPE_FIELD
import data.audit.auditmapper.AuditMapperFields.ID_FIELD
import data.audit.auditmapper.AuditMapperFields.NEW_STATE_FIELD
import data.audit.auditmapper.AuditMapperFields.OLD_STATE_FIELD
import org.bson.Document
import logic.model.Audit
import logic.model.EntityType
import java.time.LocalDateTime
import java.util.*

class AuditMapper {
    /*companion object {
        const val ID_FIELD = "id"
        const val CREATED_BY_FIELD = "createdBy"
        const val ENTITY_ID_FIELD = "entityId"
        const val ENTITY_TYPE_FIELD = "entityType"
        const val OLD_STATE_FIELD = "oldState"
        const val NEW_STATE_FIELD = "newState"
        const val DATE_FIELD = "date"
    }*/

    fun auditToDocument(audit: Audit): Document {
        return Document().apply {
            append(ID_FIELD, audit.id.toString())
            append(CREATED_BY_FIELD, audit.createdBy)
            append(ENTITY_ID_FIELD, audit.entityId)
            append(ENTITY_TYPE_FIELD, audit.entityType.name)
            append(OLD_STATE_FIELD, audit.oldState)
            append(NEW_STATE_FIELD, audit.newState)
            append(DATE_FIELD, audit.date.toString())
        }
    }

    fun documentToAudit(doc: Document): Audit {
        return Audit(
            id = UUID.fromString(doc.getString(ID_FIELD)),
            createdBy = doc.getString(CREATED_BY_FIELD),
            entityId = doc.getString(ENTITY_ID_FIELD),
            entityType = EntityType.valueOf(doc.getString(ENTITY_TYPE_FIELD)),
            oldState = doc.getString(OLD_STATE_FIELD),
            newState = doc.getString(NEW_STATE_FIELD),
            date = LocalDateTime.parse(doc.getString(DATE_FIELD))
        )
    }

}