package data.project.model

import org.bson.codecs.pojo.annotations.BsonProperty
import java.util.UUID

data class StateDto(
    val id: String = UUID.randomUUID().toString(),
    @BsonProperty("name")
    val name: String
)