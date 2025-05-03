package logic.validation

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class DateTimeParserImpl : DateTimeParser {
    private val formatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("yyyy/M/d H:mm") // 24-hour format

    override fun getStringFromDate(dateTime: LocalDateTime): String {
        return dateTime.format(formatter)
    }

    override fun parseDateFromString(dateString: String): LocalDateTime {
        return try {
            LocalDateTime.parse(dateString, formatter)
        } catch (e: DateTimeParseException) {
            throw IllegalArgumentException("Invalid date format: $dateString. Expected format: yyyy/MM/dd HH:mm")
        }
    }
}
