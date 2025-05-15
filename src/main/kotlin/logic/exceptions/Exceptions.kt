package logic.exceptions

open class AppException(msg: String) : Exception(msg)

class TaskNotFoundException : AppException("Task not found")

class InvalidAudit : AppException("Audit Is Invalid.")

class NoAuditsFoundException : AppException("No audit logs found for this entity ID.")

class NoTasksFoundException : AppException("No tasks found")

class InvalidTaskDateException : AppException("Invalid task date")

class DuplicateStateException(state: String) : AppException("TaskState '$state' already exists in project")

class NoProjectsFoundException : AppException("No projects Found")

class ProjectNotFoundException : AppException("Project Not Found")

class ProjectStateNotFoundException : AppException("TaskState Not Found")

class UserAlreadyExistsException(username: String) : AppException("Username '$username' already exists")

class CanNotParseUserException : AppException("Cannot parse User data from CSV")

class CanNotParseProjectException : AppException("can't parse string to project")

class CanNotParseStateException : AppException("can't parse string to TaskState")

class ShortPasswordException : AppException("Password must be at least 8 characters long")

class NoUpperCaseInPasswordException : AppException("Password must contain at least one uppercase letter")

class NoLowerCaseInPasswordException : AppException("Password must contain at least one lowercase letter")

class NoNumberInPasswordException : AppException("Password must contain at least one number")

class NoSpecialCharsInPasswordException : AppException("Password must contain at least one special character")

class InvalidCredentialsException : AppException("Invalid credentials")

class EmptyUsernameException : AppException("Username cannot be empty")

class NoLoggedInUserException : AppException("No user is currently logged in")

class UserTypeNotFoundException : AppException("User type is not found in data")

class DateFormatException (dateString:String, expectedFormat:String) : AppException("Invalid date format: $dateString. Expected format: $expectedFormat")

class NetworkErrorException(): AppException("There are Network Error")

class UnknownDataBaseException(): AppException("There are Unknown DataBase Error")

class DatabaseNotFoundException(): AppException("Database not found")


