package squad.abudhabi.logic.exceptions

open class DataException(msg: String): Exception(msg)

class FileDoesNotExistException: DataException("file does not exist")

class CanNotParseProjectException: DataException("can't parse string to project")

class CanNotParseStateException: DataException("can't parse string to State")
