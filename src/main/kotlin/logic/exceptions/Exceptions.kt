package squad.abudhabi.logic.exceptions

open class AppException(message: String) : Exception(message)

class WrongInputException : AppException("Invalid Input")
class InvalidAudit : AppException("Invalid Audit")
class EmptyList : AppException("Empty List")
