package data.audit.model

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty
import java.util.Date

data class AuditDto(
    val id: String,
    @BsonProperty("createdBy")
    val createdBy: String,
    @BsonProperty("entityId")
    val entityId: String,
    @BsonProperty("entityType")
    val entityType: String,
    @BsonProperty("oldState")
    val oldState: String,
    @BsonProperty("newState")
    val newState: String,
    @BsonProperty("date")
    val date: Date
)