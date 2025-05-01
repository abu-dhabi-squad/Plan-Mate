package squad.abudhabi.data.authentication.datasource

import squad.abudhabi.logic.model.User
import squad.abudhabi.logic.model.UserType

class CsvUserParser {
    fun parseUserToString(user: User): String {
        return " "
    }

    fun parseStringToUser(line: String): User {
        return User("", "", "", UserType.MATE)
    }
}