package data.authentication.datasource

import data.authentication.datasource.UserColumnIndex.ID
import data.authentication.datasource.UserColumnIndex.PASSWORD
import data.authentication.datasource.UserColumnIndex.USERNAME
import data.authentication.datasource.UserColumnIndex.USER_PARTS
import data.authentication.datasource.UserColumnIndex.USER_TYPE
import logic.exceptions.CanNotParseUserException
import logic.model.User
import logic.model.UserType

class CsvUserParser {
    fun parseUserToString(user: User): String {
        return "${user.id},${user.username},${user.password},${user.userType}"
    }

    fun parseStringToUser(line: String): User {
        val parts = line.split(",")
        if (parts.size != USER_PARTS) throw CanNotParseUserException()
        return User(
            id = parts[ID],
            username = parts[USERNAME],
            password = parts[PASSWORD],
            userType = UserType.valueOf(parts[USER_TYPE])
        )
    }

}
