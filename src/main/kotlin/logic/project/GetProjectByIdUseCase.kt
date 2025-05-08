package logic.project

import logic.exceptions.ProjectNotFoundException
import logic.model.Project
import logic.repository.ProjectRepository

class GetProjectByIdUseCase(private val projectRepository: ProjectRepository) {
    operator fun invoke(projectId: String): Project {
        return projectRepository.getProjectById(projectId) ?: throw ProjectNotFoundException()
    }
}