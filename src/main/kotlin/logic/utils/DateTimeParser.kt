package presentation.logic.utils

import java.time.LocalDateTime

interface DateTimeParser {
    fun getStringFromDate(dateTime: LocalDateTime): String
    fun parseDateFromString(dateString: String): LocalDateTime
}