package squad.abudhabi.logic.project

import squad.abudhabi.logic.exceptions.ProjectNotFoundException
import squad.abudhabi.logic.repository.ProjectRepository

class EditProjectUseCase(
    private val projectRepository: ProjectRepository
) {
    fun editProject(projectId: String, newName: String){
        val project = projectRepository.getProjectById(projectId)
            ?: throw ProjectNotFoundException(projectId)
        projectRepository.editProject(project.copy(projectName = newName))
    }
}