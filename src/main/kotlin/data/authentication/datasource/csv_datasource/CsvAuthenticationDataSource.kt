package data.authentication.datasource.csv_datasource

import data.authentication.datasource.localdatasource.LocalAuthenticationDataSource
import data.utils.filehelper.FileHelper
import logic.model.User

class CsvAuthenticationDataSource(
    private val csvUserParser: CsvUserParser,
    private val fileHelper: FileHelper,
    private val filePath: String
) : LocalAuthenticationDataSource {
    override suspend fun getUserByUserName(userName: String): User? {
        return getAllUsers().find { it.username == userName }
    }

    override suspend fun getAllUsers(): List<User> {
        return fileHelper.readFile(filePath).map { csvUserParser.parseStringToUser(it) }
    }

    override suspend fun createUser(user: User) {
        saveUsers(getAllUsers().plus(user))
    }

    private fun saveUsers(users: List<User>) {
        fileHelper.appendFile(filePath, users.map(csvUserParser::parseUserToString))
    }

}