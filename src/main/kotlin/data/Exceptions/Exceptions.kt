package squad.abudhabi.data.Exceptions

open class DataException(msg: String): Exception(msg)

class FileDoesNotExistException(): DataException("file does not exist")

class NoProjectsFoundException(): DataException("no projects found in the data")