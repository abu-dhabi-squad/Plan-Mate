package squad.abudhabi.data.utils.parser

import squad.abudhabi.data.utils.mapper.Mapper
import java.io.FileReader

class CsvParser : Parser {
    override fun <T> parse(
        fileReader: FileReader,
        mapper: Mapper<T>
    ): List<T> {
        return parseCsvLines(fileReader).drop(1)
            .asSequence()
            .map { mapper.map(it) }
            .toList()
    }

    private fun parseCsvLine(line: String): List<String> {
        val result = mutableListOf<String>()
        val current = StringBuilder()
        var insideQuotes = false
        var i = 0

        while (i < line.length) {
            val c = line[i]
            when (c) {
                '"' -> {
                    if (insideQuotes && i + 1 < line.length && line[i + 1] == '"') {
                        current.append('"')
                        i++
                    } else {
                        insideQuotes = !insideQuotes
                    }
                }

                ',' -> {
                    if (insideQuotes) current.append(c)
                    else {
                        result.add(current.toString())
                        current.clear()
                    }
                }

                else -> current.append(c)
            }
            i++
        }
        result.add(current.toString())
        return result
    }

    private fun parseCsvLines(fileReader: FileReader): List<List<String>> {
        val rows = mutableListOf<List<String>>()
        val buffer = StringBuilder()
        var insideQuotes = false
        fileReader.readLines().forEach { rawLine ->
            buffer.appendLine(rawLine)
            val quoteCount = rawLine.count { it == '"' }
            insideQuotes =
                if (insideQuotes) quoteCount % 2 == 0 else quoteCount % 2 != 0

            if (!insideQuotes) {
                val parsed = parseCsvLine(buffer.toString().trim())
                rows.add(parsed)
                buffer.clear()
            }
        }

        return rows
    }

}