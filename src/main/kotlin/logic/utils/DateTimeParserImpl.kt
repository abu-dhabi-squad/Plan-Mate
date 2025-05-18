package logic.utils

import logic.exceptions.DateFormatException
import kotlinx.datetime.LocalDateTime


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
        // Split date and time parts by 'T'
        val parts = dateTimeStr.split("T")
        if (parts.size != 2) return dateTimeStr // Unexpected format

        val datePart = parts[0]
        val timePart = parts[1]

        // Normalize date part (yyyy-M-d -> yyyy-MM-dd)
        val dateSegments = datePart.split("-")
        if (dateSegments.size != 3) return dateTimeStr
        val year = dateSegments[0]
        val month = dateSegments[1].padStart(2, '0')
        val day = dateSegments[2].padStart(2, '0')

        // Normalize time part (H:m:s or H:m -> HH:mm:ss)
        val timeSegments = timePart.split(":")
        if (timeSegments.size !in 2..3) return dateTimeStr
        val hour = timeSegments[0].padStart(2, '0')
        val minute = timeSegments[1].padStart(2, '0')
        val second = if (timeSegments.size == 3) timeSegments[2].padStart(2, '0') else "00"

        return "$year-$month-$day" + "T" + "$hour:$minute:$second"
    }
}
