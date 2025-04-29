package squad.abudhabi.logic.exceptions

open class DataException(msg: String): Exception(msg)

class FileDoesNotExistException: DataException("file does not exist")

class CanNotParseProjectException: DataException("can't parse string to project")

class CanNotParseStateException: DataException("can't parse string to State")

open class AppException(msg: String): Exception(msg)

class CanNotEditException: AppException("can't edit project")

class DataNotFoundException: AppException("there is no data found")