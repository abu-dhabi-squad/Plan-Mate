package data.authentication.model

import org.bson.codecs.pojo.annotations.BsonProperty

data class UserDto(
    val id: String,
    @BsonProperty("username")
    val username: String,

    @BsonProperty("password")
    val password: String,

    @BsonProperty("userType")
    val userType: String
)