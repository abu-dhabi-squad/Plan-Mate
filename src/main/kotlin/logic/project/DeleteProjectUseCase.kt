package squad.abudhabi.logic.project

import squad.abudhabi.logic.exceptions.ProjectNotFoundException
import squad.abudhabi.logic.repository.ProjectRepository

class DeleteProjectUseCase(private val projectRepository: ProjectRepository) {
    fun execute(projectId: String): Boolean {
        projectRepository.getProjects().find { it.id == projectId }
            ?: throw ProjectNotFoundException(projectId)

        return projectRepository.deleteProject(projectId)
    }
}