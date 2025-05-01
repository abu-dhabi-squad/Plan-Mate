package squad.abudhabi.data.authentication.datasource

import squad.abudhabi.logic.exceptions.CanNotParseUserException
import squad.abudhabi.logic.model.User
import squad.abudhabi.logic.model.UserType

class CsvUserParser {
    fun parseUserToString(user: User): String {
        return "${user.id},${user.username},${user.password},${user.userType}"
    }

    fun parseStringToUser(line: String): User {
        val parts = line.split(",")
        if (parts.size != 4) throw CanNotParseUserException()
        return User(parts[0], parts[1], parts[2], UserType.valueOf(parts[3]))
    }
}