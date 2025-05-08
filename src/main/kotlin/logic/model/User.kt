package logic.model

import java.util.UUID

data class User(
    val id: String = UUID.randomUUID().toString(),
    val username: String,
    val password: String,
    val userType: UserType
)