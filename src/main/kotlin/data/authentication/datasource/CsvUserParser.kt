package squad.abudhabi.data.authentication.datasource

import squad.abudhabi.data.authentication.datasource.UserColumnIndex.ID
import squad.abudhabi.data.authentication.datasource.UserColumnIndex.PASSWORD
import squad.abudhabi.data.authentication.datasource.UserColumnIndex.USERNAME
import squad.abudhabi.data.authentication.datasource.UserColumnIndex.USER_PARTS
import squad.abudhabi.data.authentication.datasource.UserColumnIndex.USER_TYPE
import squad.abudhabi.logic.exceptions.CanNotParseUserException
import squad.abudhabi.logic.model.User
import squad.abudhabi.logic.model.UserType

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
            userType = UserType.valueOf(parts[USER_TYPE]))
    }
}