package logic.project

import squad.abudhabi.logic.exceptions.ProjectNotFoundException
import squad.abudhabi.logic.repository.ProjectRepository

class DeleteProjectUseCase(private val projectRepository: ProjectRepository) {
    suspend operator fun invoke(projectId: String) {
        projectRepository.getProjectById(projectId)
            ?: throw ProjectNotFoundException()
        projectRepository.deleteProject(projectId)
    }
}