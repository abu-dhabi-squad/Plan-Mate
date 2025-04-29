package squad.abudhabi.logic.validation

import squad.abudhabi.logic.exceptions.InvalidTaskDateException


class TaskValidatorImpl(
    private val dateValidator: DateValidator,
    private val dateParser: DateParser
) : TaskValidator {

    override fun validateOrThrow(startDate: String, endDate: String) {
        try {
            dateValidator.validateDateOrThrow(startDate)
            dateValidator.validateDateOrThrow(endDate)
            val start = dateParser.parseDateFromString(startDate)
            val end = dateParser.parseDateFromString(endDate)
            if (end.isBefore(start)) throw InvalidTaskDateException("End date cannot be before start date")
        } catch (ex: Exception) {
            throw InvalidTaskDateException("" + ex.message)
        }
    }
}
