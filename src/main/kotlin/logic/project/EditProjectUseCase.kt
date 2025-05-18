package logic.project

import logic.exceptions.ProjectNotFoundException
import logic.repository.ProjectRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
@OptIn(ExperimentalUuidApi::class)
class EditProjectUseCase(
    private val projectRepository: ProjectRepository
) {
    suspend operator fun invoke(projectId: Uuid, newName: String) {
        val project = projectRepository.getProjectById(projectId)
            ?: throw ProjectNotFoundException()
        projectRepository.editProject(project.copy(projectName = newName))
    }
}