package squad.abudhabi.data.authentication.datasource

import squad.abudhabi.data.utils.filehelper.FileHelper
import squad.abudhabi.logic.model.User

class CsvAuthenticationDataSource(
    private val csvParser: CsvUserParser,
    private val fileHelper: FileHelper,
    private val filePath: String
) :AuthenticationDataSource {
    override fun getAllUsers(): List<User> {
        return listOf()
    }

    override fun saveUsers(users: List<User>) {
        TODO("Not yet implemented")
    }

}