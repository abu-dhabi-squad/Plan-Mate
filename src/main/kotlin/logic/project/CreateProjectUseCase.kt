package logic.project

import logic.model.Project
import logic.model.State
import logic.repository.ProjectRepository

class CreateProjectUseCase(
    private val projectRepository: ProjectRepository
) {
    operator fun invoke(newProject: Project){
        projectRepository.addProject(newProject)
    }
}