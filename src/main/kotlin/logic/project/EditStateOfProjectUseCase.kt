package squad.abudhabi.logic.project

import squad.abudhabi.logic.model.Project
import squad.abudhabi.logic.model.State
import squad.abudhabi.logic.repository.ProjectRepository

class EditStateOfProjectUseCase(
    private val projectRepository: ProjectRepository
) {
    fun editStateOfProject(project: Project, newState:State):Boolean{
        return false
    }
}