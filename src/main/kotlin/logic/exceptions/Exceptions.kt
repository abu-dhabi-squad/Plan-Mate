package squad.abudhabi.logic.exceptions

open class DomainException(error: String): Exception(error)
class TaskNotFoundException: DomainException("Task not found")
class NoTasksFoundException: DomainException("No tasks found" )
class InvalidDateFormatException: DomainException("Invalid date format")
class InvalidYearException: DomainException("Invalid year")
class InvalidTaskDateException: DomainException("Invalid task date")