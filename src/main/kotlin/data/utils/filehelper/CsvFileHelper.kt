package data.utils.filehelper

import java.io.File

class CsvFileHelper : FileHelper {
    override fun readFile(fileName: String): List<String> {
        val file = File(fileName)
        if (!file.exists()) {
            file.createNewFile()
        }
        return File(fileName).readLines()

    }


    override fun writeFile(fileName: String, data: List<String>) {
        val file = File(fileName)
        if (data.isEmpty()) throw IllegalArgumentException("Data cannot be empty")
        file.writeText(data.joinToString("\n") + "\n")
    }

    override fun appendFile(fileName: String, data: List<String>) {
        val file = File(fileName)
        if (!file.exists())
            file.createNewFile()
        if (data.isEmpty()) throw IllegalArgumentException("Data cannot be empty")
        file.appendText(data.joinToString("\n") + "\n")
    }

}