package squad.abudhabi.logic.exceptions

open class AppException(msg: String): Exception(msg)

class InvalidStateException(message: String) : AppException(message)

class DuplicateStateException(state: String) : AppException("State '$state' already exists in project")

class NoProjectsFoundException : AppException("No projects Found")

class ProjectNotFoundException(id: String) : AppException("No Project Found with ID: $id")

class ProjectStateNotFoundException(id: String) : AppException("No State Found with id: $id in the project")

class InvalidProjectNameException: AppException(("Project name cannot be blank"))

open class DataException(msg: String): AppException(msg)

class FileDoesNotExistException: DataException("file does not exist")

class CanNotParseProjectException: DataException("can't parse string to project")

class CanNotParseStateException: DataException("can't parse string to State")
