package logic.utils

import java.time.LocalDate

interface DateParser {
    fun parseDateFromString(date: String): LocalDate
    fun getStringFromDate(date: LocalDate): String
}