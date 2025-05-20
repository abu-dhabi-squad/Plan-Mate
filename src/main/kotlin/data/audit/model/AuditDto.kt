package data.audit.model

import org.bson.codecs.pojo.annotations.BsonId

data class AuditDto(
    @BsonId val _id: String,
    val createdBy: String,
    val entityId: String,
    val entityType: String,
    val oldState: String,
    val newState: String,
    val date: Long
)