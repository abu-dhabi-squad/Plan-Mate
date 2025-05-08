package data.audit.model

import org.bson.codecs.pojo.annotations.BsonId
import java.util.Date

data class AuditDto(
    @BsonId
    val id: String ,
    val createdBy: String,
    val entityId: String,
    val entityType: String,
    val oldState: String,
    val newState: String,
    val date: Date
)