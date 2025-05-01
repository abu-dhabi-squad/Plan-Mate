package squad.abudhabi.logic.validation

import squad.abudhabi.logic.exceptions.InvalidDateFormatException
import squad.abudhabi.logic.exceptions.InvalidYearException
import java.time.LocalDate

class DateValidatorImpl(
    private val dateParserInterface: DateParser
) : DateValidator {

    override fun validateDateOrThrow(date: String) {
        val formatedDate: LocalDate
        try {
            formatedDate = dateParserInterface.parseDateFromString(date)
        } catch (e: Exception) {
            throw InvalidDateFormatException()
        }

        validateYearOrThrow(formatedDate.year)
    }

    private fun validateYearOrThrow(year: Int) {
        if (year < 2000) throw InvalidYearException()
    }

}
