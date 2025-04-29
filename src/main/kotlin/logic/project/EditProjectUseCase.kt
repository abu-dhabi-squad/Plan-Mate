package squad.abudhabi.logic.project

import squad.abudhabi.logic.model.Project
import squad.abudhabi.logic.repository.ProjectRepository

class EditProjectUseCase(
    private val projectRepository: ProjectRepository
) {

    fun editProject(oldProject: Project, newProject: Project): Boolean {
        return false
    }
}