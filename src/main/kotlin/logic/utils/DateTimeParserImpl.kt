package logic.utils

import kotlinx.datetime.LocalDateTime
import logic.exceptions.DateFormatException

class DateTimeParserImpl : DateTimeParser {

    override fun getStringFromDate(dateTime: LocalDateTime): String {
        return dateTime.toString()
    }

    override fun parseDateFromString(dateString: String): LocalDateTime {
        return try {
            val normalized = normalize(dateString.trim())
            LocalDateTime.parse(normalized)
        } catch (e: Exception) {
            throw DateFormatException(dateString, "Expected ISO-8601 format: yyyy-MM-ddTHH:mm:ss")
        }
    }

    private fun normalize(dateTimeStr: String): String {
        val parts = dateTimeStr.split(DATE_TIME_SEPARATOR)
        if (parts.size != 2) return dateTimeStr

        val datePart = parts[0]
        val timePart = parts[1]

        val dateSegments = datePart.split(DATE_SEPARATOR)
        val expectedDateParts = 3
        if (dateSegments.size != expectedDateParts) return dateTimeStr

        val (year, rawMonth, rawDay) = dateSegments
        val month = rawMonth.padStart(2, '0')
        val day = rawDay.padStart(2, '0')

        val timeSegments = timePart.split(TIME_SEPARATOR)
        if (timeSegments.size !in 2..3) return dateTimeStr

        val hour = timeSegments[0].padStart(2, '0')
        val minute = timeSegments[1].padStart(2, '0')
        val second = if (timeSegments.size == 3) timeSegments[2].padStart(2, '0') else "00"

        return "$year-$month-$day$DATE_TIME_SEPARATOR$hour:$minute:$second"
    }

    companion object {
        private const val DATE_TIME_SEPARATOR = "T"
        private const val DATE_SEPARATOR = "-"
        private const val TIME_SEPARATOR = ":"
    }

}
