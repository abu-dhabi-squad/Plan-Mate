package data.project.model

import org.bson.codecs.pojo.annotations.BsonId

data class StateDto(
    @BsonId
    val _id: String,
    val name: String
)