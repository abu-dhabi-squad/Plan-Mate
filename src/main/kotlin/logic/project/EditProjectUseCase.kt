package logic.project

import squad.abudhabi.logic.exceptions.ProjectNotFoundException
import squad.abudhabi.logic.repository.ProjectRepository

class EditProjectUseCase(
    private val projectRepository: ProjectRepository
) {
    operator fun invoke(projectId: String, newName: String){
        val project = projectRepository.getProjectById(projectId)
            ?: throw ProjectNotFoundException()
        projectRepository.editProject(project.copy(projectName = newName))
    }
}