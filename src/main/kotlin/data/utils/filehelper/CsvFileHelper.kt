package squad.abudhabi.data.utils.filehelper

import java.io.File
import java.io.FileNotFoundException

class CsvFileHelper : FileHelper {
    override fun readFile(file: File) = file.readLines()

    override fun writeFile(file: File, data: List<String>): Boolean {
        if (data.isEmpty()) throw IllegalArgumentException("Data cannot be empty")
        if(!file.exists()) throw FileNotFoundException()
        file.writeText(data.joinToString("\n"))
        return true
    }
}