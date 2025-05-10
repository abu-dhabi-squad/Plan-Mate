package logic.project

import logic.model.Project
import logic.repository.ProjectRepository

class CreateProjectUseCase(
    private val projectRepository: ProjectRepository
) {
    suspend operator fun invoke(newProject: Project) {
        projectRepository.addProject(newProject)
    }
}