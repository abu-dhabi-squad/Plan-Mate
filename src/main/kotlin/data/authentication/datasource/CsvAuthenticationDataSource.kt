package squad.abudhabi.data.authentication.datasource

import squad.abudhabi.data.utils.filehelper.FileHelper
import squad.abudhabi.logic.model.User

class CsvAuthenticationDataSource(
    private val csvUserParser: CsvUserParser,
    private val fileHelper: FileHelper,
    private val filePath: String
) :AuthenticationDataSource {

    override fun getAllUsers(): List<User> {
        return fileHelper.readFile(filePath).map { csvUserParser.parseStringToUser(it) }
    }

    override fun saveUsers(users: List<User>) {
        val usersCsv = users.map(csvUserParser::parseUserToString)

        when {
            users.isEmpty() -> fileHelper.writeFile(filePath, usersCsv)
            else -> fileHelper.appendFile(filePath, usersCsv)
        }
    }

}