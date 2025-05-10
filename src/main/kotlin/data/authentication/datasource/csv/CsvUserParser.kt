package data.authentication.datasource.csv

import logic.exceptions.CanNotParseUserException
import logic.model.User
import logic.model.UserType
import java.util.UUID


class CsvUserParser {
    fun parseUserToString(user: User): String {
        return "${user.id},${user.username},${user.password},${user.userType}"
    }

    fun parseStringToUser(line: String): User {
        val parts = line.split(",")
        if (parts.size != USER_PARTS) throw CanNotParseUserException()
        return User(
            id = UUID.fromString(parts[ID]),
            username = parts[USERNAME],
            password = parts[PASSWORD],
            userType = UserType.valueOf(parts[USER_TYPE])
        )
    }

    private companion object{
        const val ID = 0
        const val USERNAME = 1
        const val PASSWORD = 2
        const val USER_TYPE = 3
        const val USER_PARTS = 4
    }
}