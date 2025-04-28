package squad.abudhabi.data.utils.filehelper

import java.io.File

interface FileHelper {
    fun readFile(files: File): List<String>
    fun writeFile(file: File, data: List<String>): Boolean
}
