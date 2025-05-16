package helper

import logic.model.User
import logic.model.User.UserType
import java.util.UUID

fun createUser(
    userId: UUID = UUID.randomUUID(),
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