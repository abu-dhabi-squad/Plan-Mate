package squad.abudhabi.logic.exceptions

open class AppException(msg: String): Exception(msg)
class InvalidStateException(message: String) : AppException(message)
class DuplicateStateException(state: String) : AppException("State '$state' already exists in project")
class NoProjectsFoundException : AppException("No projects Found")
class ProjectNotFoundException(id: String) : AppException("No Project Found with ID: $id")
class InvalidProjectNameException: AppException(("Project name cannot be blank"))
class CanNotEditException: AppException("can't edit project")
