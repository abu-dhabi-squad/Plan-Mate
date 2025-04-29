package squad.abudhabi.logic.project

import squad.abudhabi.logic.exceptions.NoProjectsFoundException
import squad.abudhabi.logic.exceptions.ProjectNotFoundException
import squad.abudhabi.logic.model.Project
import squad.abudhabi.logic.repository.ProjectRepository

class GetAllProjectsUseCase(private val projectRepository: ProjectRepository) {
    operator fun invoke(): List<Project> {
        val storedProjects = projectRepository.getProjects()
        if (storedProjects.isEmpty()) throw NoProjectsFoundException()
        return storedProjects
    }
}