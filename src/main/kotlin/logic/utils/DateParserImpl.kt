package presentation.logic.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DateParserImpl : DateParser {
    override fun parseDateFromString(date: String): LocalDate = LocalDate.parse(date, getDateFormat())

    override fun getStringFromDate(date: LocalDate): String = date.format(getDateFormat()).toString()

    private fun getDateFormat() = DateTimeFormatter.ofPattern("yyyy-M-d")
}