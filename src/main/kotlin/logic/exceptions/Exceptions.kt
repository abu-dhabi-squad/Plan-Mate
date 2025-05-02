package squad.abudhabi.logic.exceptions

open class AppException(msg: String) : Exception(msg)

class TaskNotFoundException : AppException("Task not found")

class InvalidAudit : AppException("Audit Is Invalid")

class WrongInputException : AppException("Wrong Input")

class EmptyList: AppException("List Is Empty")

class NoTasksFoundException : AppException("No tasks found")

class InvalidDateFormatException : AppException("Invalid date format")

class InvalidYearException : AppException("Invalid year")

class InvalidTaskDateException : AppException("Invalid task date")

class DuplicateStateException(state: String) : AppException("State '$state' already exists in project")

class NoProjectsFoundException : AppException("No projects Found")

class ProjectNotFoundException() : AppException("Project Not Found")

class ProjectStateNotFoundException() : AppException("State Not Found")

open class DataException(msg: String) : AppException(msg)

class CanNotParseProjectException : DataException("can't parse string to project")

class CanNotParseStateException : DataException("can't parse string to State")
