package squad.abudhabi.data.utils.filehelper

import java.io.File

class CsvFileHelper : FileHelper {
    override fun readFile(fileName: String): List<String> {
        val file = File(fileName)
        return  file.readLines()
    }


    override fun writeFile(fileName: String, data: List<String>): Boolean {
        val file = File(fileName)
        if (data.isEmpty()) throw IllegalArgumentException("Data cannot be empty")
        file.writeText(data.joinToString("\n"))
        return true
    }

    override fun appendFile(fileName: String, data: List<String>): Boolean {
        val file = File(fileName)
        if (data.isEmpty()) throw IllegalArgumentException("Data cannot be empty")
        file.appendText(data.joinToString("\n"))
        return true
    }

}