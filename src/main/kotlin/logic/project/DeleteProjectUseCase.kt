package logic.project

import logic.exceptions.ProjectNotFoundException
import logic.repository.ProjectRepository

class DeleteProjectUseCase(private val projectRepository: ProjectRepository) {
    suspend operator fun invoke(projectId: String) {
        projectRepository.getProjectById(projectId)
            ?: throw ProjectNotFoundException()
        projectRepository.deleteProject(projectId)
    }
}