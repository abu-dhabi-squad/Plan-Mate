package logic.project

import logic.exceptions.DuplicateStateException
import logic.exceptions.ProjectNotFoundException
import logic.model.TaskState
import logic.repository.ProjectRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class AddStateToProjectUseCase(private val projectRepository: ProjectRepository) {
    suspend operator fun invoke(projectId: Uuid, newTaskState: TaskState) {
        val project = projectRepository.getProjectById(projectId)
            ?: throw ProjectNotFoundException()

        if (project.taskStates.isNotEmpty()) {
            project.taskStates.find { !it.stateName.equals(newTaskState.stateName, ignoreCase = true) }
                ?: throw DuplicateStateException(newTaskState.stateName)
        }

        val updatedProject = project.copy(taskStates = project.taskStates + newTaskState)
        projectRepository.editProject(updatedProject)
    }
}