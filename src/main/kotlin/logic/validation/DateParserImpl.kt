package squad.abudhabi.logic.validation

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DateParserImpl: DateParser {
    override fun parseDateFromString(date: String): LocalDate {
        val formatter = DateTimeFormatter.ofPattern("yyyy-M-d")
        return LocalDate.parse(date, formatter)
    }
}