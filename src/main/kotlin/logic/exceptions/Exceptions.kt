package squad.abudhabi.logic.exceptions

open class AppException(msg: String) : Exception(msg)

class NoUpperCaseInPasswordException : Exception("Password must contain at least one uppercase letter")

class NoLowerCaseInPasswordException : Exception("Password must contain at least one lowercase letter")

class NoNumberInPasswordException : Exception("Password must contain at least one number")

class InvalidTaskDateException : AppException("Invalid task date")

class DuplicateStateException(state: String) : AppException("State '$state' already exists in project")

class NoProjectsFoundException : AppException("No projects Found")

class ProjectNotFoundException() : AppException("Project Not Found")

class ProjectStateNotFoundException() : AppException("State Not Found")

open class DataException(msg: String) : AppException(msg)

class CanNotParseProjectException : DataException("can't parse string to project")

class CanNotParseStateException : DataException("can't parse string to State")

class UserAlreadyExistsException(username: String) :
    Exception("Username '$username' already exists")

class InvalidPasswordException(password: String) :
    Exception("Invalid password")

class UserNotFoundException(username: String) :
    RuntimeException("User with username '$username' not found")

class RepositoryException(message: String, cause: Throwable? = null) :
    RuntimeException(message, cause)

class InvalidCredentialsException : RuntimeException("Invalid credentials")

