package squad.abudhabi.data.utils.filehelper

import java.io.File

interface FileHelper {
    fun readFile(file: File): List<String>
    fun writeFile(fileName: String, data: List<String>): Boolean
    fun appendFile(fileName: String, data: List<String>): Boolean
}