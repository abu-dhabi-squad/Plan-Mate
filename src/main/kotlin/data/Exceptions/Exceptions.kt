package squad.abudhabi.data.Exceptions

open class DataException(msg: String): Exception(msg)

class FileDoesNotExistException: DataException("file does not exist")

class NoProjectsFoundException: DataException("no projects found in the data")

class CanNotParseProjectException: DataException("can't parse string to project")

class CanNotParseStateException: DataException("can't parse string to State")

class ProjectNotInListException: DataException("no project found in the data with this id")