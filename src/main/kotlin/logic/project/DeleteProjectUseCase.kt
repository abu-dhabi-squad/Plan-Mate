package logic.project

import logic.exceptions.ProjectNotFoundException
import logic.repository.ProjectRepository
import logic.repository.TaskRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class DeleteProjectUseCase(
    private val projectRepository: ProjectRepository,
    private val taskRepository: TaskRepository,
) {
    suspend operator fun invoke(projectId: Uuid) {
        projectRepository.getProjectById(projectId) ?: throw ProjectNotFoundException()
        projectRepository.deleteProjectById(projectId)
        taskRepository.deleteTasksByProjectById(projectId)
    }
}