package squad.abudhabi.logic.project

import squad.abudhabi.logic.model.Project
import squad.abudhabi.logic.model.State
import squad.abudhabi.logic.repository.ProjectRepository

class EditStateToProjectUseCase(
    private val projectRepository: ProjectRepository
) {

    fun editStateProject(project: Project, newState:State):Boolean{
        return false
    }


}