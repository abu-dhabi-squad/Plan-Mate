package squad.abudhabi.logic.project

import squad.abudhabi.logic.exceptions.NoProjectsFoundException
import squad.abudhabi.logic.exceptions.ProjectNotFoundException
import squad.abudhabi.logic.model.Project
import squad.abudhabi.logic.repository.ProjectRepository

class GetProjectByIdUseCase(private val projectRepository: ProjectRepository) {
    operator fun invoke(projectId: String): Project {
        val projectsStored = projectRepository.getProjects()
        if (projectsStored.isEmpty()) throw NoProjectsFoundException()
        return projectsStored.find { project -> project.id == projectId } ?: throw ProjectNotFoundException(projectId)
    }
}