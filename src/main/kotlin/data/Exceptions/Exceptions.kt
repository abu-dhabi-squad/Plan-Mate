package squad.abudhabi.data.Exceptions

open class DataException(msg: String): Exception(msg)

class FileDoesNotExistException(): DataException("file does not exist")

class NoProjectsFoundException(): DataException("no projects found in the data")

class CanNotParseProject(): DataException("can't parse string to project")

class CanNotStateProject(): DataException("can't parse string to State")