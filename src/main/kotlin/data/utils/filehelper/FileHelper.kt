package squad.abudhabi.data.utils.filehelper

import java.io.File

interface FileHelper {
    fun readFile(file: File): List<String>
    fun writeFile(file: File, data: List<String>): Boolean
    fun appendFile(file: File, data: List<String>): Boolean
    fun appendFile(file: File, data: String): Boolean
}