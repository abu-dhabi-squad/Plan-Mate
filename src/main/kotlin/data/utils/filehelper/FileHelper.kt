package squad.abudhabi.data.utils.filehelper

import java.io.File

interface FileHelper {
    fun <T> readFile(files: File): T
    fun <T> writeFile(file: File, data: List<T>)
}
