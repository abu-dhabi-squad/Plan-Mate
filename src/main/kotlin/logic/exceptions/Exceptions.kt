package squad.abudhabi.logic.exceptions

class InvalidStateException(message: String) : Exception(message)
class NoProjectsFoundException : Exception("No projects Found")
class ProjectNotFoundException(id: String) : Exception("No Project Found with ID: $id")
class InvalidProjectNameException: Exception(("Project name cannot be blank"))