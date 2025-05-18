package logic.utils

import kotlinx.datetime.LocalDate

interface DateParser {
    fun parseDateFromString(date: String): LocalDate
    fun getStringFromDate(date: LocalDate): String
}