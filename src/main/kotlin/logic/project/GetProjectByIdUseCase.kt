package squad.abudhabi.logic.project

import squad.abudhabi.logic.exceptions.ProjectNotFoundException
import squad.abudhabi.logic.model.Project
import squad.abudhabi.logic.repository.ProjectRepository

class GetProjectByIdUseCase(private val projectRepository: ProjectRepository) {
    operator fun invoke(projectId: String): Project {
        return projectRepository.getProjectById(projectId) ?: throw ProjectNotFoundException()
    }
}