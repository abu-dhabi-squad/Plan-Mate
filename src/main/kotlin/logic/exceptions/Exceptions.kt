package squad.abudhabi.logic.exceptions

open class AppException(msg: String) : Exception(msg)

class TaskNotFoundException : AppException("Task not found")

class InvalidAudit : AppException("Audit Is Invalid.")

class WrongInputException : AppException("❌ Invalid entity ID input. Please try again.")

class EmptyList: AppException("❌ No audit logs found for this entity ID.")

class NoTasksFoundException : AppException("No tasks found")

class InvalidDateFormatException : AppException("Invalid date format")

class InvalidYearException : AppException("Invalid year")

class InvalidTaskDateException : AppException("Invalid task date")

class DuplicateStateException(state: String) : AppException("State '$state' already exists in project")

class NoProjectsFoundException : AppException("No projects Found")

class ProjectNotFoundException() : AppException("Project Not Found")

class ProjectStateNotFoundException() : AppException("State Not Found")

class InvalidPasswordException(password: String) : AppException("Invalid password")

class UserAlreadyExistsException(username: String) : AppException("Username '$username' already exists")

class CanNotParseUserException : AppException("Cannot parse User data from CSV")

class UserNotFoundException(username: String) :
    AppException("User with username '$username' not found")

open class DataException(msg: String) : AppException(msg)

class CanNotParseProjectException : DataException("can't parse string to project")

class CanNotParseStateException : DataException("can't parse string to State")

open class PasswordException(msg: String) : AppException(msg)

class ShortPasswordException : PasswordException("Password must be at least 8 characters long")

class NoUpperCaseInPasswordException : PasswordException("Password must contain at least one uppercase letter")

class NoLowerCaseInPasswordException : PasswordException("Password must contain at least one lowercase letter")

class NoNumberInPasswordException : PasswordException("Password must contain at least one number")

class NoSpecialCharsInPasswordException : PasswordException("Password must contain at least one special character")

class InvalidCredentialsException : RuntimeException("Invalid credentials")

class EmptyUsernameException(message: String = "Username cannot be empty") : IllegalArgumentException(message)