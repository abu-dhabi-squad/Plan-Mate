package data.authentication.model

import org.bson.codecs.pojo.annotations.BsonId

data class UserDto(
    @BsonId
    val _id: String,
    val username: String,
    val password: String,
    val userType: String
)