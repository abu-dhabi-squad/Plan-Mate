package squad.abudhabi.logic.exceptions



class IllegalTaskException(error: String = "Invalid task data") : Exception(error)

class InvalidDateException(error: String = "Invalid date") : Exception(error)

class TaskNotFoundException(error: String = "Task not found") : Exception(error)
class NoTasksFoundException(error: String = "No tasks found" ) : Exception(error)

class InvalidDateFormatException(error: String = "Invalid date format") : Exception(error)
class InvalidYearException(error: String = "Invalid year") : Exception(error)
class InvalidTaskDateException(error: String = "Invalid task date") : Exception(error)