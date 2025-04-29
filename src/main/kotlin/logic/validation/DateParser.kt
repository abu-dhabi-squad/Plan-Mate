package squad.abudhabi.logic.validation

import java.time.LocalDate

interface DateParser {
    fun parseDateFromString(date: String): LocalDate
}