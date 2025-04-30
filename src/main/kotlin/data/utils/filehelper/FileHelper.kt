package squad.abudhabi.data.utils.filehelper


interface FileHelper {
    fun readFile(fileName: String): List<String>
    fun writeFile(fileName: String, data: List<String>): Boolean
    fun appendFile(fileName: String, data: List<String>): Boolean
}