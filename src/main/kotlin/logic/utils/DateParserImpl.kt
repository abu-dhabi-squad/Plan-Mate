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
        val dateParts = input.split(DATE_SEPARATOR)
        if (dateParts.size != EXPECTED_DATE_PARTS) return input

        val (year, rawMonth, rawDay) = dateParts
        val month = rawMonth.padStart(2, '0')
        val day = rawDay.padStart(2, '0')

        return "$year$DATE_SEPARATOR$month$DATE_SEPARATOR$day"
    }

    override fun getStringFromDate(date: LocalDate): String = date.toString()

    companion object {
        private const val DATE_SEPARATOR = "-"
        private const val EXPECTED_DATE_PARTS = 3
    }
}
