package squad.abudhabi.logic.project

import squad.abudhabi.logic.model.Project
import squad.abudhabi.logic.repository.ProjectRepository

class CreateProjectUseCase(
    private val projectRepository: ProjectRepository
) {
    operator fun invoke(projectName: String):Boolean {
        val newProject = Project(
            projectName = projectName,
            states = emptyList()
        )
        return projectRepository.addProject(newProject)
    }

}