package helper

import logic.model.User
import logic.model.User.UserType
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun createUser(
    userId: Uuid = Uuid.random(),
    username: String = "",
    userType: UserType = UserType.MATE,
    password: String = ""
): User {
    return User(
        userId = userId,
        username = username,
        userType = userType,
        password = password
    )
}