package squad.abudhabi.data.utils.filehelper

import java.io.File

class CsvFileHelper: FileHelper {
    override fun readFile(file: File): List<String> {
        return listOf()
    }

    override fun writeFile(file: File, data: List<String>): Boolean {
        return true
    }
}