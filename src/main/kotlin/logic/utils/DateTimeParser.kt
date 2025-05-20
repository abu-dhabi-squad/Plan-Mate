package logic.utils

import kotlinx.datetime.LocalDateTime

interface DateTimeParser {
    fun getStringFromDate(dateTime: LocalDateTime): String
    fun parseDateFromString(dateString: String): LocalDateTime
}