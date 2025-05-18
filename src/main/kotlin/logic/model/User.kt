package logic.model

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
@OptIn(ExperimentalUuidApi::class)
data class User(
    val userId: Uuid = Uuid.random(),
    val username: String,
    val password: String,
    val userType: UserType
){
    enum class UserType {
        ADMIN, MATE
    }
}