package squad.abudhabi.logic.project

import squad.abudhabi.logic.exceptions.CanNotEditException
import squad.abudhabi.logic.exceptions.DataNotFoundException
import squad.abudhabi.logic.model.Project
import squad.abudhabi.logic.repository.ProjectRepository

class EditProjectUseCase(
    private val projectRepository: ProjectRepository
) {
    fun editProject(newProject: Project): Boolean {
        if (newProject.states.isEmpty()) throw CanNotEditException()

        projectRepository.getProjects()
            .let { projects ->
                projects.takeIf { it.isNotEmpty() } ?: throw DataNotFoundException()
                projects.find { it.id == newProject.id } ?: throw CanNotEditException()
            }

        return projectRepository.editProject(newProject)
    }
}