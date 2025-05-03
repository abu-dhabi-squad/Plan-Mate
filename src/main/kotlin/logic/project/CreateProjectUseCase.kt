package logic.project

import squad.abudhabi.logic.model.Project
import squad.abudhabi.logic.model.State
import squad.abudhabi.logic.repository.ProjectRepository

class CreateProjectUseCase(
    private val projectRepository: ProjectRepository
) {
    operator fun invoke(newProject: Project){
        projectRepository.addProject(newProject)
    }
}