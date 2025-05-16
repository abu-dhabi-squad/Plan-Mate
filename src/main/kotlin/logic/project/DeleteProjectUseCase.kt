package logic.project

import logic.exceptions.ProjectNotFoundException
import logic.repository.ProjectRepository
import logic.repository.TaskRepository
import java.util.UUID

class DeleteProjectUseCase(
    private val projectRepository: ProjectRepository,
    private val taskRepository: TaskRepository,
) {
    suspend operator fun invoke(projectId: UUID) {
        projectRepository.getProjectById(projectId) ?: throw ProjectNotFoundException()
        projectRepository.deleteProjectById(projectId)
        taskRepository.deleteTasksByProjectById(projectId)
    }
}