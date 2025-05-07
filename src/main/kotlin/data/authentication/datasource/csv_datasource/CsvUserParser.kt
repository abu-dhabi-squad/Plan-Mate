package data.authentication.datasource.csv_datasource

import logic.exceptions.CanNotParseUserException
import logic.model.User
import logic.model.UserType

class CsvUserParser {
    fun parseUserToString(user: User): String {
        return "${user.id},${user.username},${user.password},${user.userType}"
    }

    fun parseStringToUser(line: String): User {
        val parts = line.split(",")
        if (parts.size != UserColumnIndex.USER_PARTS) throw CanNotParseUserException()
        return User(
            id = parts[UserColumnIndex.ID],
            username = parts[UserColumnIndex.USERNAME],
            password = parts[UserColumnIndex.PASSWORD],
            userType = UserType.valueOf(parts[UserColumnIndex.USER_TYPE])
        )
    }

}