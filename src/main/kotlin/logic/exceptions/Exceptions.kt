package squad.abudhabi.logic.exceptions

class NoProjectsFoundException: Exception("No projects Found")
class ProjectNotFoundException(id: String) : Exception("No Project Found with ID: $id")