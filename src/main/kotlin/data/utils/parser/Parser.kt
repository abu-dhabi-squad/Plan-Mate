package squad.abudhabi.data.utils.parser

import squad.abudhabi.data.utils.mapper.Mapper
import java.io.FileReader

interface Parser {
    fun <T> parse( fileReader: FileReader,mapper: Mapper<T>): List<T>
}