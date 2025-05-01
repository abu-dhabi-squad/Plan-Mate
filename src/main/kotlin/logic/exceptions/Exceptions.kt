package squad.abudhabi.logic.exceptions

class ShortPasswordException : Exception("Password must be at least 8 characters long")

class NoUpperCaseInPasswordException : Exception("Password must contain at least one uppercase letter")

class NoLowerCaseInPasswordException : Exception("Password must contain at least one lowercase letter")

class NoNumberInPasswordException : Exception("Password must contain at least one number")

class NoSpecialCharsInPasswordException : Exception("Password must contain at least one special character")