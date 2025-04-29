package squad.abudhabi.logic.project

import squad.abudhabi.logic.exceptions.CanNotEditException
import squad.abudhabi.logic.exceptions.DataNotFoundException
import squad.abudhabi.logic.model.Project
import squad.abudhabi.logic.model.State
import squad.abudhabi.logic.repository.ProjectRepository

class EditStateOfProjectUseCase(
    private val projectRepository: ProjectRepository
) {
    fun editStateOfProject(project: Project, newState: State): Boolean {
        if (project.states.isEmpty()) throw CanNotEditException()

        project.states.find { it.id == newState.id }
            ?: throw CanNotEditException()

        projectRepository.getProjects()
            .let { projects ->
                projects.takeIf { it.isNotEmpty() } ?: throw DataNotFoundException()
                projects.find { it.id == project.id } ?: throw CanNotEditException()
            }

        project.states.map { state -> if (state.id == newState.id) newState else state }
        return projectRepository.editProject(project)
    }
}