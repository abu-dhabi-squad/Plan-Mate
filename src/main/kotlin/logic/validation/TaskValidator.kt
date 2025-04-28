package squad.abudhabi.logic.validation

interface TaskValidator {
    fun validateOrThrow(startDate: String, endDate: String)
}