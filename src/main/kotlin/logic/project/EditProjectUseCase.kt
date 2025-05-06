package logic.project

import logic.exceptions.ProjectNotFoundException
import logic.repository.ProjectRepository

class EditProjectUseCase(
    private val projectRepository: ProjectRepository
) {
    suspend operator fun invoke(projectId: String, newName: String) {
        val project = projectRepository.getProjectById(projectId)
            ?: throw ProjectNotFoundException()
        projectRepository.editProject(project.copy(projectName = newName))
    }
}