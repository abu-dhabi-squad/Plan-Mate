package logic.project

import squad.abudhabi.logic.model.Project
import squad.abudhabi.logic.repository.ProjectRepository

class CreateProjectUseCase(
    private val projectRepository: ProjectRepository
) {
    suspend operator fun invoke(newProject: Project) {
        projectRepository.addProject(newProject)
    }
}