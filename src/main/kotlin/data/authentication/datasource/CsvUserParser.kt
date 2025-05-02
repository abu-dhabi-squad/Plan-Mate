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
        return User(
            id = parts[ID],
            username = parts[USERNAME],
            password = parts[PASSWORD],
            userType = UserType.valueOf(parts[USER_TYPE]))
    }

    companion object{
            const val ID = 0
            const val USERNAME = 1
            const val PASSWORD = 2
            const val USER_TYPE = 3
    }
}