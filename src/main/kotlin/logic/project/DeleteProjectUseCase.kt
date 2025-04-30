package squad.abudhabi.logic.project

import squad.abudhabi.logic.exceptions.ProjectNotFoundException
import squad.abudhabi.logic.repository.ProjectRepository

class DeleteProjectUseCase(private val projectRepository: ProjectRepository) {
    operator fun invoke(projectId: String): Boolean {
        projectRepository.getProjectById(projectId)
            ?: throw ProjectNotFoundException(projectId)
        return projectRepository.deleteProject(projectId)
    }
}