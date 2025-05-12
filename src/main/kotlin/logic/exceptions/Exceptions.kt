package logic.exceptions

open class AppException(msg: String) : Exception(msg)

class TaskNotFoundException : AppException("Task not found")

class InvalidAudit : AppException("Audit Is Invalid.")

class WrongInputException : AppException("Invalid entity ID input. Please try again.")

class NoAuditsFoundException : AppException("No audit logs found for this entity ID.")

class NoTasksFoundException : AppException("No tasks found")

class InvalidTaskDateException : AppException("Invalid task date")

class DuplicateStateException(state: String) : AppException("TaskState '$state' already exists in project")

class NoProjectsFoundException : AppException("No projects Found")

class ProjectNotFoundException : AppException("Project Not Found")

class ProjectStateNotFoundException : AppException("TaskState Not Found")

class UserAlreadyExistsException(username: String) : AppException("Username '$username' already exists")

class CanNotParseUserException : AppException("Cannot parse User data from CSV")

class UserNotFoundException(username: String) : AppException("User with username '$username' not found")

class CanNotParseProjectException : AppException("can't parse string to project")

class CanNotParseStateException : AppException("can't parse string to TaskState")

class ShortPasswordException : AppException("Password must be at least 8 characters long")

class NoUpperCaseInPasswordException : AppException("Password must contain at least one uppercase letter")

class NoLowerCaseInPasswordException : AppException("Password must contain at least one lowercase letter")

class NoNumberInPasswordException : AppException("Password must contain at least one number")

class NoSpecialCharsInPasswordException : AppException("Password must contain at least one special character")

class InvalidCredentialsException : RuntimeException("Invalid credentials")

class EmptyUsernameException : IllegalArgumentException("Username cannot be empty")

class NoLoggedInUserException : IllegalStateException("No user is currently logged in")

class UserTypeNotFoundException : AppException("User type is not found in data")