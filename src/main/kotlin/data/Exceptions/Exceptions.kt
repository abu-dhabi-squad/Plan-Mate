package squad.abudhabi.data.Exceptions

open class DataException(msg: String): Exception(msg)

class FileDoesNotExistException(): DataException("file does not exist")