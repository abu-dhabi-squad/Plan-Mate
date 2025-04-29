package squad.abudhabi.logic.exceptions

open class AppException(msg: String): Exception(msg)

class CanNotEditException: AppException("can't edit project")

class DataNotFoundException: AppException("there is no data found")