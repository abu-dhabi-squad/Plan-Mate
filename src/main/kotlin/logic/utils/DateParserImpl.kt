package logic.utils

import kotlinx.datetime.LocalDate
import logic.exceptions.DateFormatException

class DateParserImpl : DateParser {

    override fun parseDateFromString(date: String): LocalDate = try {
        LocalDate.parse(normalizeDate(date.trim()))
    } catch (e: Exception) {
        throw DateFormatException(date, "Expected ISO-8601 format: yyyy-MM-dd")

    }
    private fun normalizeDate(input: String): String {
        val parts = input.split("-")
        if (parts.size != 3) return input
        val year = parts[0]
        val month = parts[1].padStart(2, '0')
        val day = parts[2].padStart(2, '0')
        return "$year-$month-$day"
    }
        override fun getStringFromDate(date: LocalDate): String = date.toString()
    }