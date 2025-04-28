package squad.abudhabi.logic.exceptions



class IllegalTaskException(error: String = "Invalid task data") : Exception(error)

class InvalidDateException(error: String = "Invalid date") : Exception(error)

class TaskNotFoundException(error: String = "Task not found") : Exception(error)