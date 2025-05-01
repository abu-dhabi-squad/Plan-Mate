package squad.abudhabi.logic.exceptions

class ShortPasswordException : Exception("Password must be at least 8 characters long")

class NoUpperCaseInPasswordException : Exception("Password must contain at least one uppercase letter")

class NoLowerCaseInPasswordException : Exception("Password must contain at least one lowercase letter")

class NoNumberInPasswordException : Exception("Password must contain at least one number")

class NoSpecialCharsInPasswordException : Exception("Password must contain at least one special character")

class UserAlreadyExistsException(username: String) : Exception("Username '$username' already exists")

class InvalidPasswordException(password: String) :
    Exception("Invalid password")

class UserNotFoundException(username: String) :
    RuntimeException("User with username '$username' not found")

class InvalidCredentialsException : RuntimeException("Invalid credentials")

class EmptyUsernameException(message: String = "Username cannot be empty") : IllegalArgumentException(message)

class CanNotParseUserException : Exception("Cannot parse User data from CSV")


