package squad.abudhabi.logic.exceptions


class UserAlreadyExistsException(username: String) :
    Exception("Username '$username' already exists")

class InvalidPasswordException(password: String) :
    Exception("Invalid password")

class UserNotFoundException(username: String) :
    RuntimeException("User with username '$username' not found")

class InvalidCredentialsException : RuntimeException("Invalid credentials")

class EmptyUsernameException(message: String = "Username cannot be empty") : IllegalArgumentException(message)


